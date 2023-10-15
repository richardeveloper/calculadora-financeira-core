package br.com.calculadorafinanceira.requests;

import br.com.calculadorafinanceira.enums.TipoPeriodo;
import br.com.calculadorafinanceira.exceptions.models.ValidationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Periodo {

  private static final Integer MONTHS_OF_YEAR = 12;

  @Getter
  @NotNull(message = "O campo tipo é obrigatório.")
  private TipoPeriodo tipo;

  @Min(value = 1, message = "O campo periodo deve ser maior que zero[0].")
  @NotNull(message = "O campo periodo é obrigatório.")
  private Integer valor;

  public Integer getPeriodoMensal() {
    switch (tipo) {
      case ANUAL -> {
        return this.valor * MONTHS_OF_YEAR;
      }
      case MENSAL -> {
        return this.valor;
      }
      default -> throw new ValidationException("Desculpe, não foi possível identificar o período do tempo.");
    }
  }

  public Integer getPeriodoAnual() {
    switch (tipo) {
      case ANUAL -> {
        return this.valor;
      }
      case MENSAL -> {
        return this.valor / MONTHS_OF_YEAR;
      }
      default -> throw new ValidationException("Desculpe, não foi possível identificar o período do tempo.");
    }
  }

}
