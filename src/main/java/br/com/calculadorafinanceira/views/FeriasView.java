package br.com.calculadorafinanceira.views;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class FeriasView {

  @Builder.Default
  private BigDecimal saldoFerias = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal tercoFerias = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal abonoPecuniario = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal tercoAbonoPecuniario = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal descontoInss = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal descontoIrrf = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal totalFerias = BigDecimal.ZERO;
}
