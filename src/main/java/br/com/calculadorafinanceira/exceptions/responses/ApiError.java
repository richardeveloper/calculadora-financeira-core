package br.com.calculadorafinanceira.exceptions.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT")
  private LocalDateTime timestamp;

  private String error;

  private String message;

  private Integer status;

  private String path;

  private List<ApiValidationError> errors;

  public ApiError(String error, String message, Integer status, String path, LocalDateTime timestamp) {
    this.timestamp = timestamp;
    this.error = error;
    this.message = message;
    this.status = status;
    this.path = path;
  }

  public ApiError(String error, Integer status, String path, LocalDateTime timestamp, List<ApiValidationError> errors) {
    this.timestamp = timestamp;
    this.error = error;
    this.status = status;
    this.path = path;
    this.errors = errors;
  }
}
