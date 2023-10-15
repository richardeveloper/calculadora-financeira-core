package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ValidationException;
import br.com.calculadorafinanceira.requests.INSSRequest;
import br.com.calculadorafinanceira.requests.IRRFRequest;
import br.com.calculadorafinanceira.requests.SalarioLiquidoRequest;
import br.com.calculadorafinanceira.responses.SalarioLiquidoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class CalculadoraSalarioLiquido {

  @Autowired
  private CalculadoraINSS calculadoraINSS;

  @Autowired
  private CalculadoraIRRF calculadoraIRRF;

  public SalarioLiquidoResponse calcularSalarioLiquido(SalarioLiquidoRequest request) {

    if (request.getDescontos().compareTo(request.getSalarioBruto()) >= 0) {
      throw new ValidationException("O valor dos descontos não pode ser superior ao valor do salário bruto.");
    }

    try {

      INSSRequest inssRequest = INSSRequest.builder()
        .salarioBruto(request.getSalarioBruto())
        .build();

      BigDecimal inss = calculadoraINSS.calcularINSS(inssRequest).getInss();

      IRRFRequest irrfRequest = IRRFRequest.builder()
        .salarioBruto(request.getSalarioBruto())
        .dependentes(request.getDependentes())
        .build();

      BigDecimal irrf = calculadoraIRRF.calcularIRRF(irrfRequest).getIrrf();

      BigDecimal salarioLiquido = request.getSalarioBruto()
        .subtract(inss)
        .subtract(irrf)
        .subtract(request.getDescontos());

      return SalarioLiquidoResponse.builder()
        .salarioBruto(request.getSalarioBruto())
        .descontoInss(inss)
        .descontoIrrf(irrf)
        .salarioLiquido(salarioLiquido)
        .build();

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}
