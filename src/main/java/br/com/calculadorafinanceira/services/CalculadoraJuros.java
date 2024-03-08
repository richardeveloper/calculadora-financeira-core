package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.enums.TipoPeriodo;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.responses.JurosCompostosResponse;
import br.com.calculadorafinanceira.responses.JurosSimplesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculadoraJuros {

  private static final int PERCENTAGE_DIVISOR = 100;

  public JurosSimplesResponse calcularJurosSimples() {
    return null;
  }

  public JurosCompostosResponse calcularJurosCompostos(JurosCompostosRequest request) {

    BigDecimal valorAplicado = request.getValorAplicado();
    BigDecimal totalJuros = BigDecimal.ZERO;

    int tempoInvestimento = request.getPeriodo().getValor();
    double taxaJuros = request.getJuros().getValor();

    TipoPeriodo tipoPeriodo = request.getPeriodo().getTipo();

    switch (tipoPeriodo) {
      case ANUAL -> {
        if (request.getJuros().getTipo().equals(TipoPeriodo.MENSAL)) {
          taxaJuros = converterJurosMensalParaAnual(request.getJuros().getValor());
        }
      }
      case MENSAL -> {
        if (request.getJuros().getTipo().equals(TipoPeriodo.ANUAL)) {
          taxaJuros = converterJurosAnualParaMensal(request.getJuros().getValor());
        }
      }
      default -> throw new ServiceException("Não foi possível identificar o tipo do período.");
    }

    for (int i = 0; i < tempoInvestimento ; i++) {
      BigDecimal juros = valorAplicado.multiply(BigDecimal.valueOf(taxaJuros / PERCENTAGE_DIVISOR));
      valorAplicado = valorAplicado.add(juros);
      totalJuros = totalJuros.add(juros);
    }

    return JurosCompostosResponse.builder()
      .valorInvestido(request.getValorAplicado())
      .totalJuros(totalJuros.setScale(2, RoundingMode.FLOOR))
      .valorCorrigido(valorAplicado.setScale(2, RoundingMode.FLOOR))
      .build();
  }

  private double converterJurosMensalParaAnual(double taxaJuros) {
    double base = 1 + (taxaJuros / PERCENTAGE_DIVISOR);
    int expoente = 12;
    double perc = Math.pow(base, expoente);

    return (perc - 1) * PERCENTAGE_DIVISOR;
  }

  private double converterJurosAnualParaMensal(double taxaJuros) {
    double base = 1 + (taxaJuros / PERCENTAGE_DIVISOR);
    double expoente = 1.0 / 12;
    double perc = Math.pow(base, expoente);

    return (perc - 1) * PERCENTAGE_DIVISOR;
  }

}
