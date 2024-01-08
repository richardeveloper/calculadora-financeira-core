package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ValidationException;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
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
  private CalculadoraInss calculadoraINSS;

  @Autowired
  private CalculadoraIrrf calculadoraIRRF;

  public SalarioLiquidoResponse calcularSalarioLiquido(SalarioLiquidoRequest request) {

    if (request.getDescontos().compareTo(request.getSalarioBruto()) >= 0) {
      throw new ValidationException("O valor dos descontos não pode ser superior ao valor do salário bruto.");
    }

    try {

      InssRequest inssRequest = InssRequest.builder()
        .salarioBruto(request.getSalarioBruto())
        .build();

      BigDecimal inss = calculadoraINSS.calcularINSS(inssRequest).getInss();

      IrrfRequest irrfRequest = IrrfRequest.builder()
        .salarioBruto(request.getSalarioBruto())
        .dependentes(request.getDependentes())
        .build();

      BigDecimal irrf = calculadoraIRRF.calcularIRRF(irrfRequest).getIrrf();

      BigDecimal salarioLiquido = request.getSalarioBruto()
        .subtract(inss)
        .subtract(irrf)
        .subtract(request.getDescontos());

      return SalarioLiquidoResponse.builder()
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
