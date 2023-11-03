package br.com.calculadorafinanceira.responses;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FgtsResponse {

  @Builder.Default
  private Integer mesesTrabalhados = 0;

  @Builder.Default
  private BigDecimal depositoMensal = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal totalJuros = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal totalDepositado = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal valorCorrigido = BigDecimal.ZERO;

}
