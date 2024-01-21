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
      .valorMaximo(new BigDecimal("1320.00"))
      .aliquota(7.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroInss getSegundaFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("1320.01"))
      .valorMaximo(new BigDecimal("2571.29"))
      .aliquota(9.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

  public static ParametroInss getTerceiraFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2571.30"))
      .valorMaximo(new BigDecimal("3856.94"))
      .aliquota(12.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

  }

  public static ParametroInss getQuartaFaixaSalarial() {
    return ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("3856.95"))
      .valorMaximo(new BigDecimal("7507.49"))
      .aliquota(14.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();
  }

}
