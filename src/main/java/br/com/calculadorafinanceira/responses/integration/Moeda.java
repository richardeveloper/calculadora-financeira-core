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
public class Moeda {

  @JsonAlias(value = "code")
  private String code;

  @JsonAlias(value = "codein")
  private String codeIn;

  @JsonAlias(value = "name")
  private String name;

  @JsonAlias(value = "high")
  private String high;

  @JsonAlias(value = "low")
  private String low;

  @JsonAlias(value = "varBid")
  private String varBid;

  @JsonAlias(value = "pctChange")
  private String pctChange;

  @JsonAlias(value = "bid")
  private String bid;

  @JsonAlias(value = "ask")
  private String ask;

  @JsonAlias(value = "timestamp")
  private String timestamp;

  @JsonAlias(value = "create_date")
  private String createDate;

}
