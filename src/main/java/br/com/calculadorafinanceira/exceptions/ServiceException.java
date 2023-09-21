package br.com.calculadorafinanceira.exceptions;

public class ServiceException extends RuntimeException {

  public ServiceException(String message) {
    super(message);
  }

}
