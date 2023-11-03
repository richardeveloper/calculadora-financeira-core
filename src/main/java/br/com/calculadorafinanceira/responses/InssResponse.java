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
public class InssResponse {

  @Builder.Default
  private BigDecimal inss = BigDecimal.ZERO;

  @Builder.Default
  private Double aliquota = 0.0;

}
