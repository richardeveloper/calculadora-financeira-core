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
      .valorMaximo(new BigDecimal("1412.00"))
      .aliquota(7.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroInss getSegundaFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("1412.01"))
      .valorMaximo(new BigDecimal("2666.68"))
      .aliquota(9.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroInss getTerceiraFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2666.69"))
      .valorMaximo(new BigDecimal("4000.03"))
      .aliquota(12.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

  }

  public static ParametroInss getQuartaFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("4000.04"))
      .valorMaximo(new BigDecimal("7786.02"))
      .aliquota(14.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

}
