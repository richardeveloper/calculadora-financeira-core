package br.com.calculadorafinanceira.requests.dto;

import br.com.calculadorafinanceira.enums.TipoPeriodo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Juros {

  @NotNull(message = "O campo tipo é obrigatório.")
  private TipoPeriodo tipo;

  @Min(value = 0, message = "O campo valor deve ser maior ou igual a 0.")
  @NotNull(message = "O campo valor é obrigatório.")
  private Double valor;

}
