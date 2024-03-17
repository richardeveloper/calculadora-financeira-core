package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.enums.TipoPagamento;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.DecimoTerceiroRequest;
import br.com.calculadorafinanceira.requests.FeriasRequest;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.responses.DecimoTerceiroResponse;
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
  private CalculadoraIrrf calculadoraIrrf;

  @Autowired
  private CalculadoraInss calculadoraInss;

  @Autowired
  private CalculadoraDecimoTerceiro calculadoraDecimoTerceiro;

  private static final int DAYS_OF_MONTH = 30;
  private static final int PRECISION_SCALE = 10;
  private static final BigDecimal ONE_THIRD = new BigDecimal("3");
  private static final BigDecimal VACATION_DAYS_SELL = BigDecimal.TEN;

  public FeriasResponse calcularFerias(FeriasRequest request) throws ServiceException {

    try {

      if (request.isAbonoPecuniarioInvalido()) {
        throw new ServiceException("Ao solicitar o abono pecuniário, a quantidade máxima permitida para solicitar é de 20 dias de férias.");
      }

      BigDecimal totalDiasFerias = BigDecimal.valueOf(request.getDiasFerias());

      BigDecimal saldoTotalFerias = request.getSalarioBruto()
        .divide(BigDecimal.valueOf(DAYS_OF_MONTH), PRECISION_SCALE, RoundingMode.HALF_UP)
        .multiply(totalDiasFerias)
        .setScale(2, RoundingMode.HALF_UP);

      BigDecimal saldoTercoFerias = saldoTotalFerias.divide(ONE_THIRD, 2, RoundingMode.HALF_UP);

      BigDecimal baseParaCalculoImpostos = saldoTotalFerias.add(saldoTercoFerias);

      BigDecimal valorAbonoPecuniario = BigDecimal.ZERO;
      BigDecimal tercoAbonoPecuniario = BigDecimal.ZERO;

      BigDecimal adiantamentoDecimoTerceiro = BigDecimal.ZERO;

      if (request.isAbonoPecuniario()) {
        valorAbonoPecuniario = request.getSalarioBruto()
          .divide(BigDecimal.valueOf(DAYS_OF_MONTH), PRECISION_SCALE, RoundingMode.HALF_UP)
          .multiply(VACATION_DAYS_SELL).setScale(2, RoundingMode.HALF_UP);

        tercoAbonoPecuniario = valorAbonoPecuniario.divide(ONE_THIRD, 2, RoundingMode.HALF_UP);
      }

      if (request.isAdiantamentoDecimoTerceiro()) {
        DecimoTerceiroRequest decimoTerceiroRequest = DecimoTerceiroRequest.builder()
          .salarioBruto(request.getSalarioBruto())
          .dependentes(request.getDependentes())
          .mesesTrabalhados(12)
          .tipoPagamento(TipoPagamento.PRIMEIRA_PARCELA)
          .build();

        DecimoTerceiroResponse decimoTerceiroResponse = calculadoraDecimoTerceiro.calcularDecimoTerceiro(decimoTerceiroRequest);

        adiantamentoDecimoTerceiro = decimoTerceiroResponse.getValorAReceber();
      }

      InssRequest inssRequest = InssRequest.builder()
        .salarioBruto(baseParaCalculoImpostos)
        .build();

      BigDecimal inss = calculadoraInss.calcularInss(inssRequest).getInss();

      IrrfRequest irrfRequest = IrrfRequest.builder()
        .salarioBruto(baseParaCalculoImpostos)
        .dependentes(request.getDependentes())
        .build();

      BigDecimal irrf = calculadoraIrrf.calcularIrrf(irrfRequest).getIrrf();

      BigDecimal totalFerias = saldoTotalFerias
        .add(saldoTercoFerias)
        .add(valorAbonoPecuniario)
        .add(tercoAbonoPecuniario)
        .add(adiantamentoDecimoTerceiro)
        .subtract(inss)
        .subtract(irrf);

      return FeriasResponse.builder()
        .saldoFerias(saldoTotalFerias)
        .tercoFerias(saldoTercoFerias)
        .abonoPecuniario(valorAbonoPecuniario)
        .tercoAbonoPecuniario(tercoAbonoPecuniario)
        .adiantamentoDecimoTerceiro(adiantamentoDecimoTerceiro)
        .descontoInss(inss)
        .descontoIrrf(irrf)
        .totalFerias(totalFerias)
        .build();

    }
    catch (ServiceException e) {
      throw e;
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Desculpe, não foi possível completar a solicitação.");
    }
  }

}
