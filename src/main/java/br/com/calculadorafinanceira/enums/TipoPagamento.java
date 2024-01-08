package br.com.calculadorafinanceira.enums;

import lombok.Getter;

@Getter
public enum TipoPagamento {

  PARCELA_UNICA("PARCELA_UNICA"),
  PRIMEIRA_PARCELA("PRIMEIRA_PARCELA"),
  SEGUNDA_PARCELA("SEGUNDA_PARCELA");

  private final String descricao;

  TipoPagamento(String descricao) {
    this.descricao = descricao;
  }

  public static TipoPagamento parse(String string) {
    if (string == null) {
      return null;
    }
    for (TipoPagamento tipoPagamento : TipoPagamento.values()) {
      if (string.equalsIgnoreCase(tipoPagamento.getDescricao())) {
        return tipoPagamento;
      }
    }
    throw new AssertionError();
  }
}
