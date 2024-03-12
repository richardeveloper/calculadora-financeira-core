package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.DecimoTerceiroRequest;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.responses.DecimoTerceiroResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculadoraDecimoTerceiro {

  private static final int MONTHS_OF_YEAR = 12;
  private static final int PRECISION_SCALE = 10;

  @Autowired
  private CalculadoraIrrf calculadoraIrrf;

  @Autowired
  private CalculadoraInss calculadoraInss;

  public DecimoTerceiroResponse calcularDecimoTerceiro(DecimoTerceiroRequest request) throws ServiceException {

    try {
      BigDecimal decimoTerceiroSalario = request.getSalarioBruto()
        .divide(BigDecimal.valueOf(MONTHS_OF_YEAR), PRECISION_SCALE, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(request.getMesesTrabalhados()))
        .setScale(2, RoundingMode.HALF_UP);

      switch (request.getTipoPagamento()) {
        case PARCELA_UNICA -> {
          InssRequest inssRequest = InssRequest.builder()
            .salarioBruto(decimoTerceiroSalario)
            .build();

          BigDecimal descontoInss = calculadoraInss.calcularInss(inssRequest).getInss();

          IrrfRequest irrfRequest = IrrfRequest.builder()
            .salarioBruto(decimoTerceiroSalario)
            .dependentes(request.getDependentes())
            .build();

          BigDecimal descontoIrrf = calculadoraIrrf.calcularIrrf(irrfRequest).getIrrf();

          BigDecimal parcelaUnica = decimoTerceiroSalario
            .subtract(descontoInss)
            .subtract(descontoIrrf);

          return DecimoTerceiroResponse.builder()
            .decimoTerceiro(decimoTerceiroSalario)
            .descontoInss(descontoInss)
            .descontoIrrf(descontoIrrf)
            .valorAReceber(parcelaUnica)
            .build();
        }
        case PRIMEIRA_PARCELA -> {
          BigDecimal primeiraParcela = decimoTerceiroSalario.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
          BigDecimal descontoInss = BigDecimal.ZERO;
          BigDecimal descontoIrrf = BigDecimal.ZERO;

          return DecimoTerceiroResponse.builder()
            .decimoTerceiro(primeiraParcela)
            .descontoInss(descontoInss)
            .descontoIrrf(descontoIrrf)
            .valorAReceber(primeiraParcela)
            .build();
        }
        case SEGUNDA_PARCELA -> {
          InssRequest inssRequest = InssRequest.builder()
            .salarioBruto(decimoTerceiroSalario)
            .build();

          BigDecimal descontoInss = calculadoraInss.calcularInss(inssRequest).getInss();

          IrrfRequest irrfRequest = IrrfRequest.builder()
            .salarioBruto(decimoTerceiroSalario)
            .dependentes(request.getDependentes())
            .build();

          BigDecimal descontoIrrf = calculadoraIrrf.calcularIrrf(irrfRequest).getIrrf();

          decimoTerceiroSalario = decimoTerceiroSalario.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

          BigDecimal segundaParcela = decimoTerceiroSalario
            .subtract(descontoInss)
            .subtract(descontoIrrf);

          return DecimoTerceiroResponse.builder()
            .decimoTerceiro(decimoTerceiroSalario)
            .descontoInss(descontoInss)
            .descontoIrrf(descontoIrrf)
            .valorAReceber(segundaParcela)
            .build();
        }
        default -> {
          return DecimoTerceiroResponse.builder().build();
        }
      }

    } catch (ServiceException e) {
      throw e;
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Desculpe, não foi possível completar a solicitação.");
    }
  }
}
