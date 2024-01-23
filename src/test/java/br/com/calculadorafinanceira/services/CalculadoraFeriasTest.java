package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.DecimoTerceiroRequest;
import br.com.calculadorafinanceira.requests.FeriasRequest;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.responses.DecimoTerceiroResponse;
import br.com.calculadorafinanceira.responses.FeriasResponse;
import br.com.calculadorafinanceira.responses.InssResponse;
import br.com.calculadorafinanceira.responses.IrrfResponse;

import org.assertj.core.api.Assertions;

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

class CalculadoraFeriasTest {

  @Mock
  CalculadoraIrrf calculadoraIrrf;

  @Mock
  CalculadoraInss calculadoraInss;

  @Mock
  CalculadoraDecimoTerceiro calculadoraDecimoTerceiro;

  @InjectMocks
  CalculadoraFerias calculadoraFerias;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void calcularFerias_deveLancarExcecaoQuandoDiasFeriasSuperiorVinteComAbonoPecuniario() {

    FeriasRequest request = new FeriasRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDiasFerias(21);
    request.setDependentes(0);
    request.setAbonoPecuniario(true);
    request.setAdiantamentoDecimoTerceiro(false);

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraFerias.calcularFerias(request));

    String expectedMessage = "Ao solicitar o abono pecuniário, a quantidade máxima permitida para solicitar é de 20 dias de férias.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calcularFerias_deveCalcularValorFeriasIntegralComSucesso() {

    FeriasRequest request = new FeriasRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDiasFerias(30);
    request.setDependentes(0);
    request.setAbonoPecuniario(false);
    request.setAdiantamentoDecimoTerceiro(false);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("5.00"));

    IrrfResponse irrfResponse = new IrrfResponse();
    irrfResponse.setIrrf(new BigDecimal("5.00"));

    when(calculadoraInss.calcularInss(any(InssRequest.class))).thenReturn(inssResponse);

    when(calculadoraIrrf.calcularIrrf(any(IrrfRequest.class))).thenReturn(irrfResponse);

    FeriasResponse response = calculadoraFerias.calcularFerias(request);

    assertThat(response).isNotNull();
    assertThat(response.getSaldoFerias()).isEqualTo(new BigDecimal("50.00"));
    assertThat(response.getTercoFerias()).isEqualTo(new BigDecimal("16.67"));
    assertThat(response.getAbonoPecuniario()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getTercoAbonoPecuniario()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getAdiantamentoDecimoTerceiro()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getDescontoInss()).isEqualTo(new BigDecimal("5.00"));
    assertThat(response.getDescontoIrrf()).isEqualTo(new BigDecimal("5.00"));
    assertThat(response.getTotalFerias()).isEqualTo(new BigDecimal("56.67"));
  }

  @Test
  void calcularFerias_deveCalcularValorFeriasParcialComAbonoPecuniario() {

    FeriasRequest request = new FeriasRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDiasFerias(10);
    request.setDependentes(0);
    request.setAbonoPecuniario(true);
    request.setAdiantamentoDecimoTerceiro(false);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("5.00"));

    IrrfResponse irrfResponse = new IrrfResponse();
    irrfResponse.setIrrf(new BigDecimal("5.00"));

    when(calculadoraInss.calcularInss(any(InssRequest.class))).thenReturn(inssResponse);

    when(calculadoraIrrf.calcularIrrf(any(IrrfRequest.class))).thenReturn(irrfResponse);

    FeriasResponse response = calculadoraFerias.calcularFerias(request);

    assertThat(response).isNotNull();
    assertThat(response.getSaldoFerias()).isEqualTo(new BigDecimal("16.67"));
    assertThat(response.getTercoFerias()).isEqualTo(new BigDecimal("5.56"));
    assertThat(response.getAbonoPecuniario()).isEqualTo(new BigDecimal("16.67"));
    assertThat(response.getTercoAbonoPecuniario()).isEqualTo(new BigDecimal("5.56"));
    assertThat(response.getAdiantamentoDecimoTerceiro()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getDescontoInss()).isEqualTo(new BigDecimal("5.00"));
    assertThat(response.getDescontoIrrf()).isEqualTo(new BigDecimal("5.00"));
    assertThat(response.getTotalFerias()).isEqualTo(new BigDecimal("34.46"));
  }

  @Test
  void calcularFerias_deveCalcularValorFeriasParcialComAdiantamentoDecimoTerceiroSalario() {

    FeriasRequest request = new FeriasRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDiasFerias(30);
    request.setDependentes(0);
    request.setAbonoPecuniario(false);
    request.setAdiantamentoDecimoTerceiro(true);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("5.00"));

    IrrfResponse irrfResponse = new IrrfResponse();
    irrfResponse.setIrrf(new BigDecimal("5.00"));

    DecimoTerceiroResponse decimoTerceiroResponse = new DecimoTerceiroResponse();
    decimoTerceiroResponse.setDecimoTerceiro(new BigDecimal("10.00"));
    decimoTerceiroResponse.setDescontoInss(new BigDecimal("2.00"));
    decimoTerceiroResponse.setDescontoIrrf(new BigDecimal("3.00"));
    decimoTerceiroResponse.setValorAReceber(new BigDecimal("5.00"));

    when(calculadoraInss.calcularInss(any(InssRequest.class))).thenReturn(inssResponse);

    when(calculadoraIrrf.calcularIrrf(any(IrrfRequest.class))).thenReturn(irrfResponse);

    when(calculadoraDecimoTerceiro.calcularDecimoTerceiro(any(DecimoTerceiroRequest.class)))
      .thenReturn(decimoTerceiroResponse);

    FeriasResponse response = calculadoraFerias.calcularFerias(request);

    assertThat(response).isNotNull();
    assertThat(response.getSaldoFerias()).isEqualTo(new BigDecimal("50.00"));
    assertThat(response.getTercoFerias()).isEqualTo(new BigDecimal("16.67"));
    assertThat(response.getAbonoPecuniario()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getTercoAbonoPecuniario()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getAdiantamentoDecimoTerceiro()).isEqualTo(new BigDecimal("5.00"));
    assertThat(response.getDescontoInss()).isEqualTo(new BigDecimal("5.00"));
    assertThat(response.getDescontoIrrf()).isEqualTo(new BigDecimal("5.00"));
    assertThat(response.getTotalFerias()).isEqualTo(new BigDecimal("61.67"));
  }

  @Test
  void calcularIrrf_deveLancarExcecaoQuandoOcorrerErroInesperado() {

    FeriasRequest request = new FeriasRequest();
    request.setSalarioBruto(new BigDecimal("50.00"));
    request.setDiasFerias(30);
    request.setDependentes(0);
    request.setAbonoPecuniario(false);
    request.setAdiantamentoDecimoTerceiro(false);

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenThrow(new RuntimeException("Erro inesperado."));

    ServiceException exception = assertThrows(ServiceException.class, () -> calculadoraFerias.calcularFerias(request));

    String expectedMessage = "Desculpe, não foi possível completar a solicitação.";

    Assertions.assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }
}