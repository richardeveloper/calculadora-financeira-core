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
public class FeriasRequest {

  @Min(value = 0, message = "O campo salarioBruto deve ser maior ou igual a zero[0].")
  @NotNull(message = "O campo salarioBruto é obrigatório.")
  private BigDecimal salarioBruto;

  @Min(value = 0, message = "O campo dependentes deve ser maior ou igual a zero[0].")
  @Max(value = 10, message = "O campo dependentes deve ser inferior a dez[10].")
  @NotNull(message = "O campo dependentes é obrigatório.")
  private Integer dependentes;

  @Min(value = 1, message = "O campo diasFerias deve ser maior que zero[0].")
  @Max(value = 30, message = "O campo diasFerias não pode ser maior que trinta[30].")
  @NotNull(message = "O campo diasFerias é obrigatório.")
  private Integer diasFerias;

  @NotNull(message = "O campo abonoPecuniario é obrigatório.")
  private boolean abonoPecuniario;

  @NotNull(message = "O campo adiantamentoDecimoTerceiro é obrigatório.")
  private boolean adiantamentoDecimoTerceiro;

}
