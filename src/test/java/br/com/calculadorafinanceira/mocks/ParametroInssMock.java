package br.com.calculadorafinanceira.mocks;

import br.com.calculadorafinanceira.entities.ParametroInss;
import br.com.calculadorafinanceira.enums.FaixaSalarialInss;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParametroInssMock {

  public static ParametroInss getPrimeiraFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL)
      .valorMinimo(BigDecimal.ZERO)
      .valorMaximo(new BigDecimal("10.00"))
      .aliquota(1.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroInss getSegundaFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("10.01"))
      .valorMaximo(new BigDecimal("20.00"))
      .aliquota(2.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroInss getTerceiraFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("20.01"))
      .valorMaximo(new BigDecimal("30.00"))
      .aliquota(3.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

  }

  public static ParametroInss getQuartaFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("30.01"))
      .valorMaximo(new BigDecimal("40.00"))
      .aliquota(4.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

}
