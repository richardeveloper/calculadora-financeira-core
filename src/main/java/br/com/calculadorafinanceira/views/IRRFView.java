package br.com.calculadorafinanceira.views;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class IRRFView {

  private BigDecimal irrf;
  private Double aliquota;

}
