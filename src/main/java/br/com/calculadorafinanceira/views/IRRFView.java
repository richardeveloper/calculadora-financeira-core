package br.com.calculadorafinanceira.views;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class IRRFView {

  @Builder.Default
  private BigDecimal irrf = BigDecimal.ZERO;

  @Builder.Default
  private Double aliquota = 0.0;

}
