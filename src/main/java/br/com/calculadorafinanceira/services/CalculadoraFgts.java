package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.FgtsRequest;
import br.com.calculadorafinanceira.responses.FgtsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class CalculadoraFgts {

  private static final BigDecimal BASE_PARCELA_FGTS = new BigDecimal("8");
  private static final BigDecimal JUROS_CORRECAO_FGTS = new BigDecimal("3");

  public FgtsResponse calcularFgts(FgtsRequest request) throws ServiceException {

    try {
      if (request.getDataSaida().isBefore(request.getDataEntrada())) {
        throw new ServiceException("A data de saída do colaborador na empresa deve ser superior a data de entrada.");
      }

      long mesesTrabalhados = ChronoUnit.MONTHS.between(request.getDataEntrada(), request.getDataSaida());

      BigDecimal depositoMensal = request.getSalarioBruto()
        .multiply(BASE_PARCELA_FGTS)
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

      BigDecimal totalDepositado = depositoMensal.multiply(BigDecimal.valueOf(mesesTrabalhados));

      return FgtsResponse.builder()
        .depositoMensal(depositoMensal)
        .mesesTrabalhados((int) mesesTrabalhados)
        .totalDepositado(totalDepositado)
        .build();

    } catch (ServiceException e) {
      throw e;
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Desculpe, não foi possível completar a solicitação.");
    }
  }
}
