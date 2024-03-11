package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.enums.TipoPeriodo;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
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

  private static final int PERCENTAGE_DIVISOR = 100;

  public JurosSimplesResponse calcularJurosSimples(JurosSimplesRequest request) {

    BigDecimal valorCorrigido = request.getValorAplicado();
    BigDecimal totalJuros = BigDecimal.ZERO;

    double taxaJuros = request.getJuros().getValor();
    int tempoInvestimento = request.getPeriodo().getValor();

    TipoPeriodo tipoPeriodo = request.getPeriodo().getTipo();

    switch (tipoPeriodo) {
      case ANUAL -> {
        if (request.getJuros().getTipo().equals(TipoPeriodo.MENSAL)) {
          taxaJuros = converterJurosMensalParaAnual(taxaJuros);
        }
      }
      case MENSAL -> {
        if (request.getJuros().getTipo().equals(TipoPeriodo.ANUAL)) {
          taxaJuros = converterJurosAnualParaMensal(taxaJuros);
        }
      }
      default -> throw new ServiceException("Não foi possível identificar o tipo do período.");
    }

    BigDecimal juros = valorCorrigido
      .multiply(BigDecimal.valueOf(taxaJuros))
      .multiply(BigDecimal.valueOf(tempoInvestimento));

    valorCorrigido = valorCorrigido.add(juros);
    totalJuros = totalJuros.add(juros);

    return JurosSimplesResponse.builder()
      .valorInvestido(request.getValorAplicado())
      .totalJuros(totalJuros)
      .valorCorrigido(valorCorrigido)
      .build();
  }

  public JurosCompostosResponse calcularJurosCompostos(JurosCompostosRequest request) {

    int tempoInvestimento = request.getPeriodo().getValor();

    double taxaJuros = request.getJuros().getValor();

    BigDecimal valorCorrigido = request.getValorAplicado();
    BigDecimal totalJuros = BigDecimal.ZERO;

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
      valorCorrigido = valorCorrigido.add(request.getDepositoMensal());

      BigDecimal juros = valorCorrigido.multiply(BigDecimal.valueOf(taxaJuros / PERCENTAGE_DIVISOR));

      valorCorrigido = valorCorrigido.add(juros);
      totalJuros = totalJuros.add(juros);
    }

    return JurosCompostosResponse.builder()
      .valorInvestido(request.getValorAplicado())
      .totalJuros(totalJuros.setScale(2, RoundingMode.FLOOR))
      .valorCorrigido(valorCorrigido.setScale(2, RoundingMode.FLOOR))
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
