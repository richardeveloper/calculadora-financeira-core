package br.com.calculadorafinanceira.enums;

import lombok.Getter;

@Getter
public enum FaixaSalarialInss {

  PRIMEIRA_FAIXA_SALARIAL("PRIMEIRA_FAIXA_SALARIAL"),
  SEGUNDA_FAIXA_SALARIAL("SEGUNDA_FAIXA_SALARIAL"),
  TERCEIRA_FAIXA_SALARIAL("TERCEIRA_FAIXA_SALARIAL"),
  QUARTA_FAIXA_SALARIAL("QUARTA_FAIXA_SALARIAL");

  private String descricao;

  FaixaSalarialInss(String descricao) {
    this.descricao = descricao;
  }

  public static FaixaSalarialInss parse(String string) {
    if (string == null) {
      return null;
    }
    for (FaixaSalarialInss faixaSalarial : FaixaSalarialInss.values()) {
      if (string.equalsIgnoreCase(faixaSalarial.getDescricao())) {
        return faixaSalarial;
      }
    }
    throw new AssertionError();
  }

}
