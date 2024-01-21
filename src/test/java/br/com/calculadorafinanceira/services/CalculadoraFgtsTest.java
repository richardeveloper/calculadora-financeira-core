package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.FgtsRequest;
import br.com.calculadorafinanceira.responses.FgtsResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

class CalculadoraFgtsTest {

  @InjectMocks
  CalculadoraFgts calculadoraFgts;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void calcularFgts_deveLancarExcecaoQuandoDataSaidaMaiorQueDataEntrada() {

    FgtsRequest request = new FgtsRequest();
    request.setSalarioBruto(new BigDecimal("1"));
    request.setDataEntrada(LocalDate.now().plusDays(1));
    request.setDataSaida(LocalDate.now());

    ServiceException exception = assertThrows(ServiceException.class, () -> calculadoraFgts.calcularFgts(request));

    String expectedMessage = "A data de saída do colaborador na empresa deve ser superior a data de entrada.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  public void calcularFgts_deveCalcularFgtsComSucesso() {

    FgtsRequest request = new FgtsRequest();
    request.setSalarioBruto(new BigDecimal("2200.00"));
    request.setDataEntrada(LocalDate.now().minusMonths(5));
    request.setDataSaida(LocalDate.now());

    FgtsResponse response = calculadoraFgts.calcularFgts(request);

    assertThat(response).isNotNull();
    assertThat(response.getDepositoMensal()).isEqualTo(new BigDecimal("176.00"));
    assertThat(response.getTotalDepositado()).isEqualTo(new BigDecimal("880.00"));
  }

}