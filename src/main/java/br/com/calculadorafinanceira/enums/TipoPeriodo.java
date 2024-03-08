package br.com.calculadorafinanceira.enums;

import lombok.Getter;

@Getter
public enum TipoPeriodo {

  MENSAL("MENSAL"),
  ANUAL("ANUAL");

  private final String descricao;

  TipoPeriodo(String descricao) {
    this.descricao = descricao;
  }

  public static TipoPeriodo parse(String descricao) {
    if (descricao == null) {
      return null;
    }
    for (TipoPeriodo tipoPeriodo : TipoPeriodo.values()) {
      if (tipoPeriodo.getDescricao().equalsIgnoreCase(descricao)) {
        return tipoPeriodo;
      }
    }
    throw new AssertionError();
  }
}
