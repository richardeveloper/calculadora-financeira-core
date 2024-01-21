package br.com.calculadorafinanceira.requests;

import jakarta.validation.constraints.Max;
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
public class IrrfRequest {

  @Min(value = 0, message = "O campo salarioBruto deve ser maior ou igual a 0.")
  @NotNull(message = "O campo salarioBruto é obrigatório.")
  private BigDecimal salarioBruto;

  @Min(value = 0, message = "O campo dependentes deve ser maior ou igual a 0.")
  @Max(value = 10, message = "O campo dependentes deve ser inferior a 10.")
  @NotNull(message = "O campo dependentes é obrigatório.")
  private Integer dependentes;

}
