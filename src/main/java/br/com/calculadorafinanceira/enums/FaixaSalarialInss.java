package br.com.calculadorafinanceira.enums;

import lombok.Getter;

@Getter
public enum FaixaSalarialInss {

  PRIMEIRA_FAIXA_SALARIAL("PRIMEIRA_FAIXA_SALARIAL"),
  SEGUNDA_FAIXA_SALARIAL("SEGUNDA_FAIXA_SALARIAL"),
  TERCEIRA_FAIXA_SALARIAL("TERCEIRA_FAIXA_SALARIAL"),
  QUARTA_FAIXA_SALARIAL("QUARTA_FAIXA_SALARIAL");

  private final String descricao;

  FaixaSalarialInss(String descricao) {
    this.descricao = descricao;
  }

  public static FaixaSalarialInss parse(String descricao) {
    if (descricao == null) {
      return null;
    }
    for (FaixaSalarialInss faixaSalarial : FaixaSalarialInss.values()) {
      if (faixaSalarial.getDescricao().equalsIgnoreCase(descricao)) {
        return faixaSalarial;
      }
    }
    throw new AssertionError();
  }

}
