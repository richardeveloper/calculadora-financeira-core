package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.requests.JurosSimplesRequest;
import br.com.calculadorafinanceira.responses.JurosCompostosResponse;
import br.com.calculadorafinanceira.responses.JurosSimplesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculadoraJuros {

  public JurosSimplesResponse calcularJurosSimples(JurosSimplesRequest request) {

    try {
      switch (request.getJuros().getTipo()) {
        case MENSAL -> {

          BigDecimal totalJuros = request.getValorInicial()
            .multiply(request.getJuros().getValorMensal())
            .multiply(BigDecimal.valueOf(request.getPeriodo().getPeriodoMensal()))
            .setScale(2, RoundingMode.HALF_UP);

          BigDecimal valorCorrigido = request.getValorInicial().add(totalJuros);

          return JurosSimplesResponse.builder()
            .valorInvestido(request.getValorInicial())
            .totalJuros(totalJuros)
            .valorCorrigido(valorCorrigido)
            .build();
        }
        case ANUAL -> {

          BigDecimal totalJuros = request.getValorInicial()
            .multiply(request.getJuros().getValorAnual())
            .multiply(BigDecimal.valueOf(request.getPeriodo().getPeriodoAnual()))
            .setScale(2, RoundingMode.HALF_UP);

          BigDecimal valorCorrigido = request.getValorInicial().add(totalJuros);

          return JurosSimplesResponse.builder()
            .valorInvestido(request.getValorInicial())
            .totalJuros(totalJuros)
            .valorCorrigido(valorCorrigido)
            .build();
        }
        default -> {
          return JurosSimplesResponse.builder().build();
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  public JurosCompostosResponse calcularJurosCompostos(JurosCompostosRequest request) {

    try {
      BigDecimal valorInvestido = request.getValorInicial();
      BigDecimal totalJuros = BigDecimal.ZERO;

      Integer periodo;
      BigDecimal taxaJuros;

      switch (request.getPeriodo().getTipo()) {
        case MENSAL -> {
          periodo = request.getPeriodo().getPeriodoMensal();
          taxaJuros = request.getJuros().getValorMensal();
        }
        case ANUAL -> {
          periodo = request.getPeriodo().getPeriodoAnual();
          taxaJuros = request.getJuros().getValorAnual();
        }
        default -> {
          return JurosCompostosResponse.builder().build();
        }
      }

      for (int i = 0; i < periodo; i++) {

        valorInvestido = valorInvestido.add(request.getValorMensal());

        BigDecimal rendimento = valorInvestido
          .multiply(taxaJuros)
          .setScale(2, RoundingMode.HALF_UP);

        valorInvestido = valorInvestido.add(rendimento);
        totalJuros = totalJuros.add(rendimento);
      }

      return JurosCompostosResponse.builder()
        .valorInvestido(valorInvestido.subtract(totalJuros))
        .totalJuros(totalJuros)
        .valorCorrigido(valorInvestido)
        .build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

}
