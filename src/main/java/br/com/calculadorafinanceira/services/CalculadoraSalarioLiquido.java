package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.requests.SalarioLiquidoRequest;
import br.com.calculadorafinanceira.responses.SalarioLiquidoResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Slf4j
@Service
public class CalculadoraSalarioLiquido {

  private static final Integer PRECISION_SCALE = 10;

  @Autowired
  private CalculadoraInss calculadoraInss;

  @Autowired
  private CalculadoraIrrf calculadoraIrrf;

  public SalarioLiquidoResponse calcularSalarioLiquido(SalarioLiquidoRequest request) throws ServiceException {

    try {

      if (request.getDescontos().compareTo(request.getSalarioBruto()) >= 0) {
        throw new ServiceException("O valor dos descontos deve ser inferior ao valor do salário bruto.");
      }

      int totalDiasMes = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

      BigDecimal salarioProporcional = request.getSalarioBruto()
        .divide(BigDecimal.valueOf(totalDiasMes), PRECISION_SCALE, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(request.getDiasTrabalhados()))
        .setScale(2, RoundingMode.HALF_UP);

      InssRequest inssRequest = InssRequest.builder()
        .salarioBruto(salarioProporcional)
        .build();

      BigDecimal inss = calculadoraInss.calcularInss(inssRequest).getInss();

      IrrfRequest irrfRequest = IrrfRequest.builder()
        .salarioBruto(salarioProporcional)
        .dependentes(request.getDependentes())
        .build();

      BigDecimal irrf = calculadoraIrrf.calcularIrrf(irrfRequest).getIrrf();

      BigDecimal salarioLiquido = salarioProporcional
        .subtract(inss)
        .subtract(irrf)
        .subtract(request.getDescontos())
        .setScale(2, RoundingMode.HALF_UP);

      return SalarioLiquidoResponse.builder()
        .descontoInss(inss)
        .descontoIrrf(irrf)
        .salarioLiquido(salarioLiquido)
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
