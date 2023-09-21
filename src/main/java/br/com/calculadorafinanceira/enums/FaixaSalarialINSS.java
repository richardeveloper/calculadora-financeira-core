package br.com.calculadorafinanceira.enums;

import lombok.Getter;

@Getter
public enum FaixaSalarialINSS {

  PRIMEIRA_FAIXA_SALARIAL("PRIMEIRA_FAIXA_SALARIAL"),
  SEGUNDA_FAIXA_SALARIAL("SEGUNDA_FAIXA_SALARIAL"),
  TERCEIRA_FAIXA_SALARIAL("TERCEIRA_FAIXA_SALARIAL"),
  QUARTA_FAIXA_SALARIAL("QUARTA_FAIXA_SALARIAL");

  private String descricao;

  FaixaSalarialINSS(String descricao) {
    this.descricao = descricao;
  }

  public static FaixaSalarialINSS parse(String string) {
    if (string == null) {
      return null;
    }
    for (FaixaSalarialINSS faixaSalarial: FaixaSalarialINSS.values()) {
      if (string.equalsIgnoreCase(faixaSalarial.getDescricao())) {
        return faixaSalarial;
      }
    }
    throw new AssertionError();
  }

}
