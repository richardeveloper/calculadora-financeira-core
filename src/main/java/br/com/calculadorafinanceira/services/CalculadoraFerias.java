package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ValidationException;
import br.com.calculadorafinanceira.requests.FeriasRequest;
import br.com.calculadorafinanceira.requests.INSSRequest;
import br.com.calculadorafinanceira.requests.IRRFRequest;
import br.com.calculadorafinanceira.responses.FeriasResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculadoraFerias {

  @Autowired
  private CalculadoraIRRF calculadoraIRRF;

  @Autowired
  private CalculadoraINSS calculadoraINSS;

  private static final Integer DAYS_OF_MONTH = 30;
  private static final Integer PRECISION_SCALE = 10;

  public FeriasResponse calcularFerias(FeriasRequest request) {

    try {

      if (request.isAbonoPecuniario() && request.getDiasFerias() > 20) {
        throw new ValidationException("Ao solicitar o abono pecuniário, a quantidade máxima permitida para solicitar é de 20 dias de férias.");
      }

      BigDecimal diasFerias = BigDecimal.valueOf(request.getDiasFerias());

      BigDecimal saldoFerias = request.getSalarioBruto()
        .divide(BigDecimal.valueOf(DAYS_OF_MONTH), PRECISION_SCALE, RoundingMode.HALF_UP)
        .multiply(diasFerias).setScale(2, RoundingMode.HALF_UP);

      BigDecimal tercoFerias = saldoFerias.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);

      BigDecimal baseParaCalculoImpostos = saldoFerias.add(tercoFerias);

      BigDecimal valorAbonoPecuniario = BigDecimal.ZERO;
      BigDecimal tercoAbonoPecuniario = BigDecimal.ZERO;

      if (request.isAbonoPecuniario()) {
        BigDecimal diasVendidos = BigDecimal.valueOf(DAYS_OF_MONTH).subtract(diasFerias);

        valorAbonoPecuniario = request.getSalarioBruto()
          .divide(BigDecimal.valueOf(DAYS_OF_MONTH), PRECISION_SCALE, RoundingMode.HALF_UP)
          .multiply(diasVendidos).setScale(2, RoundingMode.HALF_UP);

        tercoAbonoPecuniario = valorAbonoPecuniario.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
      }

      INSSRequest inssRequest = INSSRequest.builder()
        .salarioBruto(baseParaCalculoImpostos)
        .build();

      BigDecimal inss = calculadoraINSS.calcularINSS(inssRequest).getInss();

      IRRFRequest irrfRequest = IRRFRequest.builder()
        .salarioBruto(baseParaCalculoImpostos)
        .dependentes(request.getDependentes())
        .build();

      BigDecimal irrf = calculadoraIRRF.calcularIRRF(irrfRequest).getIrrf();

      BigDecimal totalFerias = saldoFerias
        .add(tercoFerias)
        .add(valorAbonoPecuniario)
        .add(tercoAbonoPecuniario)
        .subtract(inss)
        .subtract(irrf);

      return FeriasResponse.builder()
        .saldoFerias(saldoFerias)
        .tercoFerias(tercoFerias)
        .abonoPecuniario(valorAbonoPecuniario)
        .tercoAbonoPecuniario(tercoAbonoPecuniario)
        .descontoInss(inss)
        .descontoIrrf(irrf)
        .totalFerias(totalFerias)
        .build();

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

}
