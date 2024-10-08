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

  public static TipoPagamento parse(String descricao) {
    if (descricao == null) {
      return null;
    }
    for (TipoPagamento tipoPagamento : TipoPagamento.values()) {
      if (tipoPagamento.getDescricao().equalsIgnoreCase(descricao)) {
        return tipoPagamento;
      }
    }
    throw new AssertionError();
  }
}
