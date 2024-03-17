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
public class FgtsResponse {

  @Builder.Default
  private BigDecimal depositoMensal = BigDecimal.ZERO;

  @Builder.Default
  private String periodo = "";

  @Builder.Default
  private BigDecimal totalDepositado = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal jurosCorrecao = BigDecimal.ZERO;

  @Builder.Default
  private BigDecimal totalCorrigido = BigDecimal.ZERO;

}
