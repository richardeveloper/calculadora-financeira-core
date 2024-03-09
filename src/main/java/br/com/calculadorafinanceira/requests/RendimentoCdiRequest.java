package br.com.calculadorafinanceira.requests;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RendimentoCdiRequest {

  @NotNull(message = "O campo valorAplicado é obrigatório.")
  private BigDecimal valorAplicado;

  @NotNull(message = "O campo taxaRendimento é obrigatório.")
  private Double taxaRendimento;

  @NotNull(message = "O campo dataInicial é obrigatório.")
  @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
  private LocalDate dataInicial;

  @NotNull(message = "O campo dataFinal é obrigatório.")
  @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
  private LocalDate dataFinal;

}
