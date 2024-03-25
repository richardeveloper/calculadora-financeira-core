package br.com.calculadorafinanceira.mocks;

import br.com.calculadorafinanceira.entities.ParametroInssEntity;
import br.com.calculadorafinanceira.enums.FaixaSalarialInss;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ParametroInssMock {

  public static ParametroInssEntity getPrimeiraFaixaSalarial() {
    return ParametroInssEntity.builder()
      .faixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL)
      .valorMinimo(BigDecimal.ZERO)
      .valorMaximo(new BigDecimal("10.00"))
      .aliquota(1.0)
//      .dataCadastro(LocalDateTime.now())
//      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroInssEntity getSegundaFaixaSalarial() {
    return ParametroInssEntity.builder()
      .faixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("10.01"))
      .valorMaximo(new BigDecimal("20.00"))
      .aliquota(2.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroInssEntity getTerceiraFaixaSalarial() {
    return ParametroInssEntity.builder()
      .faixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("20.01"))
      .valorMaximo(new BigDecimal("30.00"))
      .aliquota(3.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroInssEntity getQuartaFaixaSalarial() {
    return ParametroInssEntity.builder()
      .faixaSalarial(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("30.01"))
      .valorMaximo(new BigDecimal("40.00"))
      .aliquota(4.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static List<ParametroInssEntity> getAllParametroInss() {
    return Arrays.asList(
      getPrimeiraFaixaSalarial(),
      getSegundaFaixaSalarial(),
      getTerceiraFaixaSalarial(),
      getQuartaFaixaSalarial()
    );
  }

}
