package br.com.calculadorafinanceira.exceptions.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT")
  private LocalDateTime timestamp;

  private String error;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String message;

  private Integer status;

  private String path;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<ApiValidationError> errors;

}
