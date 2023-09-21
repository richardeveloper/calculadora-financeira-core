package br.com.calculadorafinanceira.exceptions;

public class ValidationException extends RuntimeException {

  public ValidationException(String message) {
    super(message);
  }

}
