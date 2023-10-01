package br.com.calculadorafinanceira.exceptions.responses;

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
public class ApiValidationError {

  private String field;
  private Object rejectedValue;
  private String message;

}
