package br.com.calculadorafinanceira.requests;

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
public class JurosSimplesRequest {

  @NotNull(message = "O campo valorInicial é obrigatório.")
  private BigDecimal valorInicial;

  @NotNull(message = "O campo juros é obrigatório.")
  private Juros juros;

  @NotNull(message = "O campo periodo é obrigatório.")
  private Periodo periodo;

}
