package br.com.calculadorafinanceira.views;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class INSSView {

  @Builder.Default
  private BigDecimal inss = BigDecimal.ZERO;

  @Builder.Default
  private Double aliquota = 0.0;

}
