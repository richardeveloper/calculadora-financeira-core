package br.com.calculadorafinanceira.requests;

import br.com.calculadorafinanceira.requests.dto.Juros;
import br.com.calculadorafinanceira.requests.dto.Periodo;

import jakarta.validation.Valid;
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
public class JurosCompostosRequest {

  @Min(value = 0, message = "O campo valorAplicado deve ser maior ou igual a 0.")
  @NotNull(message = "O campo valorAplicado é obrigatório.")
  private BigDecimal valorAplicado;

  @Min(value = 0, message = "O campo depositoMensal deve ser maior ou igual a 0.")
  @NotNull(message = "O campo depositoMensal é obrigatório.")
  private BigDecimal depositoMensal;

  @Valid
  private Juros juros;

  @Valid
  private Periodo periodo;

}
