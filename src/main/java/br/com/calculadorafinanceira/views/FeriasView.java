package br.com.calculadorafinanceira.views;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class FeriasView {

  private BigDecimal saldoFerias;
  private BigDecimal tercoFerias;
  private BigDecimal abonoPecuniario;
  private BigDecimal tercoAbonoPecuniario;
  private BigDecimal descontoInss;
  private BigDecimal descontoIrrf;
  private BigDecimal totalFerias;
}
