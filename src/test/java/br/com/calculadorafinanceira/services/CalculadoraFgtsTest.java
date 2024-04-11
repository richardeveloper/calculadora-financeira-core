package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.FeriasRequest;
import br.com.calculadorafinanceira.requests.FgtsRequest;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.responses.FgtsResponse;

import br.com.calculadorafinanceira.utils.PeriodoUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class CalculadoraFgtsTest {

  @Mock
  CalculadoraJuros calculadoraJuros;

  @InjectMocks
  CalculadoraFgts calculadoraFgts;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void calcularFgts_deveCalcularValorFgtsComSucesso() {

    FgtsRequest request = new FgtsRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDataEntrada(LocalDate.now().minusMonths(5));
    request.setDataSaida(LocalDate.now());

    String infoPeriodo = PeriodoUtils.gerarInformativoPeriodo(request.getDataEntrada(), request.getDataSaida());

    when(calculadoraJuros.calcularJurosCompostos(any(JurosCompostosRequest.class))).thenCallRealMethod();

    FgtsResponse response = calculadoraFgts.calcularFgts(request);

    assertThat(response).isNotNull();

    assertThat(response.getDepositoMensal()).isEqualTo(new BigDecimal("4.00"));
    assertThat(response.getJurosCorrecao()).isEqualTo(new BigDecimal("0.14"));
    assertThat(response.getTotalDepositado()).isEqualTo(new BigDecimal("20.00"));
    assertThat(response.getTotalCorrigido()).isEqualTo(new BigDecimal("20.14"));
    assertThat(response.getPeriodo()).isEqualTo(infoPeriodo);
  }

  @Test
  void calcularFgts_deveLancarExcecaoQuandoDataSaidaSuperiorDataEntrada() {

    FgtsRequest request = new FgtsRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDataEntrada(LocalDate.now().plusDays(5));
    request.setDataSaida(LocalDate.now());

    ServiceException exception = assertThrows(ServiceException.class, () -> calculadoraFgts.calcularFgts(request));

    String expectedMessage = "A data de saída do colaborador na empresa deve ser superior a data de entrada.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calcularFgts_deveLancarExcecaoQuandoOcorrerErroInesperado() {

    FgtsRequest request = new FgtsRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDataEntrada(LocalDate.now());
    request.setDataSaida(LocalDate.now());

    when(calculadoraJuros.calcularJurosCompostos(any(JurosCompostosRequest.class)))
      .thenThrow(new RuntimeException("Erro Inesperado."));

    ServiceException exception = assertThrows(ServiceException.class, () -> calculadoraFgts.calcularFgts(request));

    String expectedMessage = "Desculpe, não foi possível completar a solicitação.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

}