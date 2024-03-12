package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.enums.TipoPeriodo;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.FgtsRequest;
import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.requests.dto.Juros;
import br.com.calculadorafinanceira.requests.dto.Periodo;
import br.com.calculadorafinanceira.responses.FgtsResponse;
import br.com.calculadorafinanceira.responses.JurosCompostosResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class CalculadoraFgts {

  private static final double PERCENTAGE_DIVISOR = 100.0;
  private static final double BASE_PARCELA_FGTS = 8.0;
  private static final double JUROS_CORRECAO_FGTS = 3.0;

  @Autowired
  private CalculadoraJuros calculadoraJuros;

  public FgtsResponse calcularFgts(FgtsRequest request) throws ServiceException {

    try {
      if (request.getDataSaida().isBefore(request.getDataEntrada())) {
        throw new ServiceException("A data de saída do colaborador na empresa deve ser superior a data de entrada.");
      }

      Long mesesTrabalhados = ChronoUnit.MONTHS.between(request.getDataEntrada(), request.getDataSaida());

      BigDecimal depositoMensal = request.getSalarioBruto()
        .multiply(BigDecimal.valueOf(BASE_PARCELA_FGTS / PERCENTAGE_DIVISOR))
        .setScale(2, RoundingMode.FLOOR);

      BigDecimal totalDepositado = depositoMensal.multiply(BigDecimal.valueOf(mesesTrabalhados));

      JurosCompostosRequest jurosCompostosRequest = JurosCompostosRequest.builder()
        .valorAplicado(BigDecimal.ZERO)
        .depositoMensal(depositoMensal)
        .juros(new Juros(TipoPeriodo.ANUAL, JUROS_CORRECAO_FGTS))
        .periodo(new Periodo(TipoPeriodo.MENSAL, mesesTrabalhados.intValue()))
        .build();

      JurosCompostosResponse jurosCompostosResponse = calculadoraJuros.calcularJurosCompostos(jurosCompostosRequest);

      return FgtsResponse.builder()
        .depositoMensal(depositoMensal)
        .mesesTrabalhados(mesesTrabalhados.intValue())
        .totalDepositado(totalDepositado)
        .jurosCorrecao(jurosCompostosResponse.getTotalJuros())
        .totalCorrigido(totalDepositado.add(jurosCompostosResponse.getTotalJuros()))
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
