package br.com.calculadorafinanceira.mocks;

import br.com.calculadorafinanceira.entities.ParametroIrrfEntity;
import br.com.calculadorafinanceira.enums.FaixaSalarialIrrf;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParametroIrrfMock {

  public static ParametroIrrfEntity getFaixaIsenta() {
    return ParametroIrrfEntity.builder()
      .faixaSalarial(FaixaSalarialIrrf.FAIXA_ISENTA)
      .valorMinimo(BigDecimal.ZERO)
      .valorMaximo(new BigDecimal("10.00"))
      .parcelaDedutivel(BigDecimal.ZERO)
      .aliquota(0.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroIrrfEntity getPrimeiraFaixaSalarial() {
    return ParametroIrrfEntity.builder()
      .faixaSalarial(FaixaSalarialIrrf.PRIMEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("10.01"))
      .valorMaximo(new BigDecimal("20.00"))
      .parcelaDedutivel(new BigDecimal("1.00"))
      .aliquota(10.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroIrrfEntity getSegundaFaixaSalarial() {
    return ParametroIrrfEntity.builder()
      .faixaSalarial(FaixaSalarialIrrf.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("20.01"))
      .valorMaximo(new BigDecimal("30.00"))
      .parcelaDedutivel(new BigDecimal("2.00"))
      .aliquota(15.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroIrrfEntity getTerceiraFaixaSalarial() {
    return ParametroIrrfEntity.builder()
      .faixaSalarial(FaixaSalarialIrrf.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("30.01"))
      .valorMaximo(new BigDecimal("40.00"))
      .parcelaDedutivel(new BigDecimal("3.00"))
      .aliquota(20.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroIrrfEntity getQuartaFaixaSalarial() {
    return ParametroIrrfEntity.builder()
      .faixaSalarial(FaixaSalarialIrrf.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("40.01"))
      .valorMaximo(new BigDecimal("999999999999.99"))
      .parcelaDedutivel(new BigDecimal("4.00"))
      .aliquota(25.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

}
