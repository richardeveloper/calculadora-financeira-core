package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.FeriasRequest;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.requests.SalarioLiquidoRequest;
import br.com.calculadorafinanceira.responses.InssResponse;
import br.com.calculadorafinanceira.responses.IrrfResponse;
import br.com.calculadorafinanceira.responses.SalarioLiquidoResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.rmi.server.ServerCloneException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculadoraSalarioLiquidoTest {

  @Mock
  CalculadoraInss calculadoraInss;

  @Mock
  CalculadoraIrrf calculadoraIrrf;

  @InjectMocks
  CalculadoraSalarioLiquido calculadoraSalarioLiquido;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void calcularSalarioLiquido_deveLancarExcecaoQuandoValorDescontosSuperiorSalarioBruto() {

    SalarioLiquidoRequest request = new SalarioLiquidoRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDependentes(0);
    request.setDescontos(new BigDecimal("150.00"));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraSalarioLiquido.calcularSalarioLiquido(request));

    String expectedMessage = "O valor dos descontos não pode ser superior ao valor do salário bruto.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calcularSalarioLiquido_deveCalcularValorSalarioLiquidoComSucesso() {

    SalarioLiquidoRequest request = new SalarioLiquidoRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDependentes(0);
    request.setDescontos(BigDecimal.ZERO);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("5.00"));

    IrrfResponse irrfResponse = new IrrfResponse();
    irrfResponse.setIrrf(new BigDecimal("5.00"));

    when(calculadoraInss.calcularInss(any(InssRequest.class))).thenReturn(inssResponse);

    when(calculadoraIrrf.calcularIrrf(any(IrrfRequest.class))).thenReturn(irrfResponse);

    SalarioLiquidoResponse response = calculadoraSalarioLiquido.calcularSalarioLiquido(request);

    assertThat(response).isNotNull();
    assertThat(response.getSalarioLiquido()).isEqualTo(new BigDecimal("40.00"));
    assertThat(response.getDescontoInss()).isEqualTo(new BigDecimal("5.00"));
    assertThat(response.getDescontoIrrf()).isEqualTo(new BigDecimal("5.00"));
  }

  @Test
  void calcularIrrf_deveLancarExcecaoQuandoOcorrerErroInesperado() {

    SalarioLiquidoRequest request = new SalarioLiquidoRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDependentes(0);
    request.setDescontos(BigDecimal.ZERO);

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenThrow(new RuntimeException("Erro inesperado."));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraSalarioLiquido.calcularSalarioLiquido(request));

    String expectedMessage = "Desculpe, não foi possível completar a solicitação.";

    Assertions.assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

}