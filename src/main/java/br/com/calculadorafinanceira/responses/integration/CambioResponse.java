package br.com.calculadorafinanceira.responses.integration;

import com.fasterxml.jackson.annotation.JsonAlias;
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
public class CambioResponse {

  @JsonAlias(value = "USDBRL")
  private Moeda dolar;

  @JsonAlias(value = "EURBRL")
  private Moeda euro;

  @JsonAlias(value = "GBPBRL")
  private Moeda libra;
}
