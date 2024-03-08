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

  @NotNull(message = "O campo porcentagemCdi é obrigatório.")
  private Double porcentagemCdi;

  @NotNull(message = "O campo dataInicio é obrigatório.")
  @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
  private LocalDate dataInicio;

  @NotNull(message = "O campo dataFim é obrigatório.")
  @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
  private LocalDate dataFim;

}
