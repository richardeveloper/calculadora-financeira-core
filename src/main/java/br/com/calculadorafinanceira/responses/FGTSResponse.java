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
public class FGTSResponse {

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
