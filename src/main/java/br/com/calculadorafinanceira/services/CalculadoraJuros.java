package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.requests.JurosSimplesRequest;
import br.com.calculadorafinanceira.responses.JurosCompostosResponse;
import br.com.calculadorafinanceira.responses.JurosSimplesResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculadoraJuros {

  public JurosSimplesResponse calcularJurosSimples(JurosSimplesRequest request) {

    try {
      BigDecimal taxaJuros = BigDecimal.ZERO;
      BigDecimal periodo = BigDecimal.ZERO;

      switch (request.getJuros().getTipo()) {
        case ANUAL -> {
          taxaJuros = request.getJuros().getValorAnual();
          periodo = BigDecimal.valueOf(request.getPeriodo().getPeriodoAnual());
        }
        case MENSAL -> {
          taxaJuros = request.getJuros().getValorMensal();
          periodo = BigDecimal.valueOf(request.getPeriodo().getPeriodoMensal());
        }
      }

      BigDecimal totalJuros = request.getValorInicial()
        .multiply(taxaJuros)
        .multiply(periodo)
        .setScale(2, RoundingMode.HALF_UP);

      BigDecimal valorCorrigido = request.getValorInicial().add(totalJuros);

      return JurosSimplesResponse.builder()
        .valorInvestido(request.getValorInicial())
        .totalJuros(totalJuros)
        .valorCorrigido(valorCorrigido)
        .build();

    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Não foi possível calcular o valor dos juros simples.");
    }
  }

  public JurosCompostosResponse calcularJurosCompostos(JurosCompostosRequest request) {

    try {
      BigDecimal valorInvestido = request.getValorInicial();
      BigDecimal totalJuros = BigDecimal.ZERO;

      Double periodo = request.getPeriodo().getPeriodoMensal();
      BigDecimal taxaJuros = request.getJuros().getValorMensal();

      for (int i = 0; i < periodo; i++) {
        BigDecimal rendimento = valorInvestido
          .multiply(taxaJuros)
          .setScale(2, RoundingMode.HALF_UP);

        valorInvestido = valorInvestido
          .add(request.getValorMensal())
          .add(rendimento);

        totalJuros = totalJuros.add(rendimento);
      }

      return JurosCompostosResponse.builder()
        .valorInvestido(valorInvestido.subtract(totalJuros))
        .totalJuros(totalJuros)
        .valorCorrigido(valorInvestido)
        .build();

    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Não foi possível calcular o valor dos juros compostos.");
    }
  }
}
