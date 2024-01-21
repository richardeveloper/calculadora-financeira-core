package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.enums.TipoPagamento;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.DecimoTerceiroRequest;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.responses.DecimoTerceiroResponse;
import br.com.calculadorafinanceira.responses.InssResponse;
import br.com.calculadorafinanceira.responses.IrrfResponse;

import org.assertj.core.api.Assertions;

import org.assertj.core.api.ZonedDateTimeAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculadoraDecimoTerceiroTest {

  @Mock
  CalculadoraIrrf calculadoraIrrf;

  @Mock
  CalculadoraInss calculadoraInss;

  @InjectMocks
  CalculadoraDecimoTerceiro calculadoraDecimoTerceiro;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void calcularDecimoTerceiro_deveCalcularPrimeiraParcelaDecimoTerceiroSalario() {

    DecimoTerceiroRequest request = new DecimoTerceiroRequest();
    request.setSalarioBruto(new BigDecimal("3500.00"));
    request.setDependentes(0);
    request.setMesesTrabalhados(12);
    request.setTipoPagamento(TipoPagamento.PRIMEIRA_PARCELA);

    DecimoTerceiroResponse response = calculadoraDecimoTerceiro.calcularDecimoTerceiro(request);

    assertThat(response).isNotNull();
    assertThat(response.getDecimoTerceiro()).isEqualTo(new BigDecimal("1750.00"));
    assertThat(response.getDescontoInss()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getDescontoIrrf()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getValorAReceber()).isEqualTo(new BigDecimal("1750.00"));
  }

  @Test
  public void calcularDecimoTerceiro_deveCalcularSegundaParcelaDecimoTerceiroSalario() {

    DecimoTerceiroRequest request = new DecimoTerceiroRequest();
    request.setSalarioBruto(new BigDecimal("3500.00"));
    request.setDependentes(0);
    request.setMesesTrabalhados(12);
    request.setTipoPagamento(TipoPagamento.SEGUNDA_PARCELA);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("318.82"));

    IrrfResponse irrfResponse = new IrrfResponse();
    irrfResponse.setIrrf(new BigDecimal("106.78"));

    when(calculadoraInss.calcularInss(any(InssRequest.class))).thenReturn(inssResponse);

    when(calculadoraIrrf.calcularIrrf(any(IrrfRequest.class))).thenReturn(irrfResponse);

    DecimoTerceiroResponse response = calculadoraDecimoTerceiro.calcularDecimoTerceiro(request);

    assertThat(response).isNotNull();
    assertThat(response.getDecimoTerceiro()).isEqualTo(new BigDecimal("1750.00"));
    assertThat(response.getDescontoInss()).isEqualTo(new BigDecimal("318.82"));
    assertThat(response.getDescontoIrrf()).isEqualTo(new BigDecimal("106.78"));
    assertThat(response.getValorAReceber()).isEqualTo(new BigDecimal("1324.40"));
  }

  @Test
  public void calcularDecimoTerceiro_deveCalcularParcelaUnicaDecimoTerceiroSalario() {

    DecimoTerceiroRequest request = new DecimoTerceiroRequest();
    request.setSalarioBruto(new BigDecimal("3500.00"));
    request.setDependentes(0);
    request.setMesesTrabalhados(12);
    request.setTipoPagamento(TipoPagamento.PARCELA_UNICA);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("318.82"));

    IrrfResponse irrfResponse = new IrrfResponse();
    irrfResponse.setIrrf(new BigDecimal("106.78"));

    when(calculadoraInss.calcularInss(any(InssRequest.class))).thenReturn(inssResponse);

    when(calculadoraIrrf.calcularIrrf(any(IrrfRequest.class))).thenReturn(irrfResponse);

    DecimoTerceiroResponse response = calculadoraDecimoTerceiro.calcularDecimoTerceiro(request);

    assertThat(response).isNotNull();
    assertThat(response.getDecimoTerceiro()).isEqualTo(new BigDecimal("3500.00"));
    assertThat(response.getDescontoInss()).isEqualTo(new BigDecimal("318.82"));
    assertThat(response.getDescontoIrrf()).isEqualTo(new BigDecimal("106.78"));
    assertThat(response.getValorAReceber()).isEqualTo(new BigDecimal("3074.40"));
  }

  @Test
  public void calcularIrrf_deveLancarExcecaoQuandoOcorrerErroEsperado() {

    DecimoTerceiroRequest request = new DecimoTerceiroRequest();
    request.setSalarioBruto(new BigDecimal(1));
    request.setDependentes(0);
    request.setMesesTrabalhados(12);
    request.setTipoPagamento(TipoPagamento.PARCELA_UNICA);

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenThrow(new ServiceException("Não foi possível recuperar informações da primeira faixa salarial."));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraDecimoTerceiro.calcularDecimoTerceiro(request));

    String expectedMessage = "Não foi possível recuperar informações da primeira faixa salarial.";

    Assertions.assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  public void calcularIrrf_deveLancarExcecaoQuandoOcorrerErroInesperado() {

    DecimoTerceiroRequest request = new DecimoTerceiroRequest();
    request.setSalarioBruto(new BigDecimal(1));
    request.setDependentes(0);
    request.setMesesTrabalhados(12);
    request.setTipoPagamento(TipoPagamento.PARCELA_UNICA);

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenThrow(new RuntimeException("Erro Inesperado."));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraDecimoTerceiro.calcularDecimoTerceiro(request));

    String expectedMessage = "Desculpe, não foi possível completar a solicitação.";

    Assertions.assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

}