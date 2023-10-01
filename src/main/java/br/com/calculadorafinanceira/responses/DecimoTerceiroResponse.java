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
public class DecimoTerceiroResponse {

  private BigDecimal valorIntegral;

  private BigDecimal primeiraParcela;

  private BigDecimal segundaParcela;
}
