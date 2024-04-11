package br.com.calculadorafinanceira.requests;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalarioLiquidoRequest {

  @Min(value = 0, message = "O campo salarioBruto deve ser maior ou igual a 0.")
  @NotNull(message = "O campo salarioBruto é obrigatório.")
  private BigDecimal salarioBruto;

  @Min(value = 0, message = "O campo dependentes deve ser maior ou igual a 0.")
  @Max(value = 10, message = "O campo dependentes deve ser inferior a 10.")
  @NotNull(message = "O campo dependentes é obrigatório.")
  private Integer dependentes;

  private Integer diasTrabalhados;

  @Min(value = 0, message = "O campo descontos deve ser maior que 0.")
  @NotNull(message = "O campo descontos é obrigatório.")
  private BigDecimal descontos;

  @Min(value = 1, message = "O campo diasTrabalhados deve ser maior ou igual a 1.")
  public Integer getDiasTrabalhados() {

    int lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

    if (diasTrabalhados == null) {
      return lastDayOfMonth;
    }

    if (diasTrabalhados > lastDayOfMonth) {
      throw new ServiceException("A quantidade de dias trabalhados não pode ser superior a quantidade de dias do mês atual.");
    }

    return diasTrabalhados;
  }

}
