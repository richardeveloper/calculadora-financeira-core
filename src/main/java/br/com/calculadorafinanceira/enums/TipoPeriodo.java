package br.com.calculadorafinanceira.enums;

import lombok.Getter;

@Getter
public enum TipoPeriodo {

  ANUAL("ANUAL"),
  MENSAL("MENSAL");

  private String descricao;

  TipoPeriodo(String descricao) {
    this.descricao = descricao;
  }

  public static TipoPeriodo parse(String string) {
    if (string == null) {
      return null;
    }
    for (TipoPeriodo tipoPeriodo : TipoPeriodo.values()) {
      if (string.equalsIgnoreCase(tipoPeriodo.getDescricao())) {
        return tipoPeriodo;
      }
    }
    throw new AssertionError();
  }
}
