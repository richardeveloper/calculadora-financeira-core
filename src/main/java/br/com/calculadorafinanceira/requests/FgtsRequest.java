package br.com.calculadorafinanceira.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
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
public class FgtsRequest {

  @Min(value = 0, message = "O campo salarioBruto deve ser maior ou igual a 0.")
  @NotNull(message = "O campo salarioBruto é obrigatório.")
  private BigDecimal salarioBruto;

  @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
  @NotNull(message = "O campo dataEntrada é obrigatório.")
  private LocalDate dataEntrada;

  @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
  @NotNull(message = "O campo dataSaida é obrigatório.")
  private LocalDate dataSaida;
}
