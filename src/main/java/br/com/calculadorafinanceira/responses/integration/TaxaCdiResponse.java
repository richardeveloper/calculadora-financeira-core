package br.com.calculadorafinanceira.responses.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxaCdiResponse {

  private int totalItems;

  private List<RegistroTaxaCdi> registros;

  private List<String> observacoes;

  private String dataAtual;

}
