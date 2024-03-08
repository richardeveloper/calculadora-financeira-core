package br.com.calculadorafinanceira.responses.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroTaxaCdi {

  private Long id;

  private String dataCotacao;

  private Double fatorDiario;

  private Double media;

  private Double mediana;

  private Double moda;

  private Double desvioPadrao;

  private Double indiceCurtose;

  private Double financeiro;

  private Integer qtdOperacoes;

  private Double taxaAnual;
}
