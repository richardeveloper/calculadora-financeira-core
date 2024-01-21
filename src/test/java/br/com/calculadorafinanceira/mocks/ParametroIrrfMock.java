package br.com.calculadorafinanceira.mocks;

import br.com.calculadorafinanceira.entities.ParametroIrrf;
import br.com.calculadorafinanceira.enums.FaixaSalarialIrrf;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParametroIrrfMock {

  public static ParametroIrrf getFaixaIsenta() {
    return ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.FAIXA_ISENTA)
      .valorMinimo(BigDecimal.ZERO)
      .valorMaximo(new BigDecimal("2112.00"))
      .parcelaDedutivel(BigDecimal.ZERO)
      .aliquota(0.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroIrrf getPrimeiraFaixaSalarial() {
    return ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.PRIMEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2112.01"))
      .valorMaximo(new BigDecimal("2826.65"))
      .parcelaDedutivel(new BigDecimal("158.40"))
      .aliquota(7.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroIrrf getSegundaFaixaSalarial() {
    return ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2826.66"))
      .valorMaximo(new BigDecimal("3751.05"))
      .parcelaDedutivel(new BigDecimal("370.40"))
      .aliquota(15.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroIrrf getTerceiraFaixaSalarial() {
    return ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("3751.06"))
      .valorMaximo(new BigDecimal("4664.68"))
      .parcelaDedutivel(new BigDecimal("651.73"))
      .aliquota(22.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroIrrf getQuartaFaixaSalarial() {
    return ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("4664.69"))
      .valorMaximo(new BigDecimal("999999999999.99"))
      .parcelaDedutivel(new BigDecimal("884.96"))
      .aliquota(27.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

}
