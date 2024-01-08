package br.com.calculadorafinanceira.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class FeriasResponse {

  @Builder.Default
  private BigDecimal saldoFerias = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal tercoFerias = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal abonoPecuniario = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal tercoAbonoPecuniario = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal adiantamentoDecimoTerceiro = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal descontoInss = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal descontoIrrf = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal totalFerias = BigDecimal.ZERO;
}
