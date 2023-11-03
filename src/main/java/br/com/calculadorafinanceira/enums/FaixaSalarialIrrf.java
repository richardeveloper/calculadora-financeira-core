package br.com.calculadorafinanceira.enums;

import lombok.Getter;

@Getter
public enum FaixaSalarialIrrf {

  FAIXA_ISENTA("FAIXA_ISENTA"),
  PRIMEIRA_FAIXA_SALARIAL("PRIMEIRA_FAIXA_SALARIAL"),
  SEGUNDA_FAIXA_SALARIAL("SEGUNDA_FAIXA_SALARIAL"),
  TERCEIRA_FAIXA_SALARIAL("TERCEIRA_FAIXA_SALARIAL"),
  QUARTA_FAIXA_SALARIAL("QUARTA_FAIXA_SALARIAL");

  private String descricao;

  FaixaSalarialIrrf(String descricao) {
    this.descricao = descricao;
  }

  public static FaixaSalarialIrrf parse(String string) {
    if (string == null) {
      return null;
    }
    for (FaixaSalarialIrrf faixaSalarial : FaixaSalarialIrrf.values()) {
      if (string.equalsIgnoreCase(faixaSalarial.getDescricao())) {
        return faixaSalarial;
      }
    }
    throw new AssertionError();
  }

}
