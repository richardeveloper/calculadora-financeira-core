package br.com.calculadorafinanceira.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InssRequest {

  @Min(value = 0, message = "O campo salarioBruto deve ser maior ou igual a 0.")
  @NotNull(message = "O campo salarioBruto é obrigatório.")
  private BigDecimal salarioBruto;

}
