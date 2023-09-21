package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.ServiceException;
import br.com.calculadorafinanceira.repositories.ParametroINSSRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculadoraINSSTest {

  @Mock
  ParametroINSSRepository parametroINSSRepository;

  @InjectMocks
  CalculadoraINSS calculadoraINSS;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void calculaINSS_deveLancarExecacaoQuandoSalarioBrutoForNulo() {
    assertThrows(ServiceException.class,
      () -> this.calculadoraINSS.calcularINSS(null));
  }

  @Test
  public void calculaINSS_deveLancarExecacaoQuandoSalarioBrutoForNegativo() {
    assertThrows(ServiceException.class,
      () -> this.calculadoraINSS.calcularINSS(BigDecimal.valueOf(-1500.00)));
  }
}