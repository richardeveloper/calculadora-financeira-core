package br.com.calculadorafinanceira.requests;

import br.com.calculadorafinanceira.enums.TipoPeriodo;
import br.com.calculadorafinanceira.exceptions.models.ValidationException;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor
public class Juros {

  private static final Integer SCALE_PRECISION = 4;
  private static final BigDecimal PERCENTAGE_DIVISOR = new BigDecimal("100");

  @Getter
  @NotNull(message = "O campo tipo é obrigatório.")
  private TipoPeriodo tipo;

  @DecimalMin(value = "0.01", message = "O campo taxaJuros deve ser maior que 0.")
  @NotNull(message = "O campo taxaJuros é obrigatório.")
  private BigDecimal valor;

  public BigDecimal getValorMensal() {
    BigDecimal taxaJuros = this.valor.divide(PERCENTAGE_DIVISOR, SCALE_PRECISION, RoundingMode.HALF_UP);

    switch (this.tipo) {
      case ANUAL -> {
        return converterJurosAnualParaMensal(taxaJuros);
      }
      case MENSAL -> {
        return taxaJuros;
      }
      default -> throw new ValidationException("Não foi possível identificar o período dos juros.");
    }
  }

  public BigDecimal getValorAnual() {
    BigDecimal taxaJuros = this.valor.divide(PERCENTAGE_DIVISOR, SCALE_PRECISION, RoundingMode.HALF_UP);

    switch (tipo) {
      case MENSAL -> {
        return converterJurosMensalParaAnual(taxaJuros);
      }
      case ANUAL -> {
        return taxaJuros;
      }
      default -> throw new ValidationException("Não foi possível identificar o período dos juros.");
    }
  }

  private BigDecimal converterJurosAnualParaMensal(BigDecimal taxaJuros) {
    double base = 1 + taxaJuros.doubleValue();
    double expoente = 1 / 12.0;

    return BigDecimal.valueOf(Math.pow(base, expoente) - 1);
  }

  private BigDecimal converterJurosMensalParaAnual(BigDecimal taxaJuros) {
    double base = 1 + taxaJuros.doubleValue();
    double expoente = 12.0;

    return BigDecimal.valueOf(Math.pow(base, expoente) - 1);
  }
}
