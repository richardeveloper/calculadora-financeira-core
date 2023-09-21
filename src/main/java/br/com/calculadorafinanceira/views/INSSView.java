package br.com.calculadorafinanceira.views;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class INSSView {

  private BigDecimal inss;
  private Double aliquota;

}
