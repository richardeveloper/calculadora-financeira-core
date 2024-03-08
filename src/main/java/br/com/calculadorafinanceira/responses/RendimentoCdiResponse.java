package br.com.calculadorafinanceira.responses;

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
public class RendimentoCdiResponse {

  private BigDecimal valorAplicado;

  private BigDecimal juros;

  private BigDecimal irrf;

  private BigDecimal valorCorrigido;

}
