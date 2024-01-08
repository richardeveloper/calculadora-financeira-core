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
public class SalarioLiquidoResponse {

  @Builder.Default
  private BigDecimal salarioLiquido = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal descontoInss = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal descontoIrrf = BigDecimal.ZERO;

}
