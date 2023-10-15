package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.enums.TipoPeriodo;
import br.com.calculadorafinanceira.exceptions.models.ValidationException;
import br.com.calculadorafinanceira.requests.FGTSRequest;
import br.com.calculadorafinanceira.requests.Juros;
import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.requests.Periodo;
import br.com.calculadorafinanceira.responses.FGTSResponse;
import br.com.calculadorafinanceira.responses.JurosCompostosResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class CalculadoraFGTS {

  private static final BigDecimal BASE_PARCELA_FGTS = new BigDecimal("8");
  private static final BigDecimal JUROS_CORRECAO_FGTS = new BigDecimal("3");

  @Autowired
  private CalculadoraJuros calculadoraJuros;

  public FGTSResponse calcularFGTS(FGTSRequest request) {

    if (request.getDataSaida().isBefore(request.getDataEntrada())) {
      throw new ValidationException("A data de saída do colaborador na empresa deve ser superior a data de entrada.");
    }

    try {
      long mesesTrabalhados = ChronoUnit.MONTHS.between(request.getDataEntrada(), request.getDataSaida());

      BigDecimal depositoMensal = request.getSalarioBruto()
        .multiply(BASE_PARCELA_FGTS)
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

      BigDecimal totalDepositado = depositoMensal.multiply(BigDecimal.valueOf(mesesTrabalhados));

      JurosCompostosRequest jurosCompostosRequest = JurosCompostosRequest.builder()
        .valorInicial(BigDecimal.ZERO)
        .valorMensal(depositoMensal)
        .juros(new Juros(TipoPeriodo.ANUAL, JUROS_CORRECAO_FGTS))
        .periodo(new Periodo(TipoPeriodo.MENSAL, (int) mesesTrabalhados))
        .build();

      JurosCompostosResponse jurosResponse = calculadoraJuros.calcularJurosCompostos(jurosCompostosRequest);

      return FGTSResponse.builder()
        .mesesTrabalhados((int) mesesTrabalhados)
        .depositoMensal(depositoMensal)
        .totalJuros(jurosResponse.getTotalJuros())
        .totalDepositado(totalDepositado)
        .valorCorrigido(jurosResponse.getValorCorrigido())
        .build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}
