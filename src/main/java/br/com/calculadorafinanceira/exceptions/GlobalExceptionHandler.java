package br.com.calculadorafinanceira.exceptions;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.exceptions.models.ValidationException;
import br.com.calculadorafinanceira.exceptions.responses.ApiError;
import br.com.calculadorafinanceira.exceptions.responses.ApiValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handlerMethodArgumentNotValidException(HttpServletRequest request,
    MethodArgumentNotValidException exception) {

    String error = "Foram encontrados dados incorretos na solicitação.";

    List<ApiValidationError> errors = exception
      .getBindingResult()
      .getFieldErrors()
      .stream()
      .map(field -> new ApiValidationError(field.getField(), field.getRejectedValue(), field.getDefaultMessage()))
      .toList();

    HttpStatus status = HttpStatus.BAD_REQUEST;
    String path = request.getRequestURI();
    LocalDateTime timestamp = LocalDateTime.now();

    ApiError apiError = new ApiError(error, status.value(), path, timestamp, errors);

    return new ResponseEntity<>(apiError, status);
  }

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<ApiError> handlerServiceException(HttpServletRequest request,
    ServiceException exception) {

    String error = "Ocorreu um erro interno durante execução do serviço.";

    String errorMessage = exception.getMessage();
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    String path = request.getRequestURI();
    LocalDateTime timestamp = LocalDateTime.now();

    ApiError apiError = new ApiError(error, errorMessage, status.value(), path, timestamp);

    return new ResponseEntity<>(apiError, status);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ApiError> handlerValidationException(HttpServletRequest request,
    ValidationException exception) {

    String error = "Ação não permitida.";

    String errorMessage = exception.getMessage();
    HttpStatus status = HttpStatus.BAD_REQUEST;
    String path = request.getRequestURI();
    LocalDateTime timestamp = LocalDateTime.now();

    ApiError apiError = new ApiError(error, errorMessage, status.value(), path, timestamp);

    return new ResponseEntity<>(apiError, status);
  }
}
