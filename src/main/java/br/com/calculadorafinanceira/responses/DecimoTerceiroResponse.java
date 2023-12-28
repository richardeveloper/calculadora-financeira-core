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

  @Builder.Default
  private BigDecimal parcelaUnica = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal primeiraParcela = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal segundaParcela = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal descontoInss = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal descontoIrrf = BigDecimal.ZERO;
}
