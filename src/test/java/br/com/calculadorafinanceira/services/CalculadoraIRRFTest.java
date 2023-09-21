package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroIRRF;
import br.com.calculadorafinanceira.exceptions.ServiceException;
import br.com.calculadorafinanceira.repositories.ParametroIRRFRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CalculadoraIRRFTest {

  @Mock
  ParametroIRRFRepository parametroIRRFRepository;

  @InjectMocks
  CalculadoraIRRF calculadoraIRRF;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void calculaIRRF_deveLancarExecacaoQuandoSalarioBrutoForNulo() {
    assertThrows(ServiceException.class,
      () -> this.calculadoraIRRF.calcularIRRF(null, 0));
  }

  @Test
  public void calculaIRRF_deveLancarExecacaoQuandoSalarioBrutoForNegativo() {
    assertThrows(ServiceException.class,
      () -> this.calculadoraIRRF.calcularIRRF(BigDecimal.valueOf(-1500.00), 0));
  }

  @Test
  public void calculaIRRF_deveLancarExecacaoQuandoDependentesMenorQueZero() {
    assertThrows(ServiceException.class,
      () -> this.calculadoraIRRF.calcularIRRF(BigDecimal.valueOf(1582.33), -2));
  }

  @Test
  public void calculaIRRF_deveLancarExecacaoQuandoDependentesVazio() {
    assertThrows(ServiceException.class,
      () -> this.calculadoraIRRF.calcularIRRF(BigDecimal.valueOf(2468.12), null));
  }

  @Test
  public void calculaIRRF_deveLancarExecacaoQuandoDependentesExcederLimiteDeDezPessoas() {
    assertThrows(ServiceException.class,
      () -> this.calculadoraIRRF.calcularIRRF(BigDecimal.valueOf(3965.48), 11));
  }
}