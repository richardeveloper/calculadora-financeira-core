package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroIrrf;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.mocks.ParametroIrrfMock;
import br.com.calculadorafinanceira.repositories.ParametroIrrfRepository;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.responses.InssResponse;
import br.com.calculadorafinanceira.responses.IrrfResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculadoraIrrfTest {

  @Mock
  ParametroIrrfRepository parametroIrrfRepository;

  @Mock
  CalculadoraInss calculadoraInss;

  @InjectMocks
  CalculadoraIrrf calculadoraIrrf;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void calcularIrrf_deveRetornarZeroQuandoSalarioBrutoIgualZero() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(BigDecimal.ZERO);
    request.setDependentes(0);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(BigDecimal.ZERO);
    inssResponse.setAliquota(0.0);

    ParametroIrrf faixaIsenta = ParametroIrrfMock.getFaixaIsenta();

    when(parametroIrrfRepository.findBySalarioBruto(BigDecimal.ZERO))
      .thenReturn(Optional.of(faixaIsenta));

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenReturn(inssResponse);

    IrrfResponse response = calculadoraIrrf.calcularIrrf(request);

    assertThat(response).isNotNull();
    assertThat(response.getIrrf()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getAliquota()).isEqualTo(0.0);
  }

  @Test
  void calcularIrrf_deveCalcularValorIrrfIsento() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("2000.00"));
    request.setDependentes(0);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(BigDecimal.ZERO);
    inssResponse.setAliquota(0.0);

    ParametroIrrf faixaIsenta = ParametroIrrfMock.getFaixaIsenta();

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("2000.00")))
      .thenReturn(Optional.of(faixaIsenta));

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenReturn(inssResponse);

    IrrfResponse response = calculadoraIrrf.calcularIrrf(request);

    assertThat(response).isNotNull();
    assertThat(response.getIrrf()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getAliquota()).isEqualTo(0.0);
  }

  @Test
  void calcularIrrf_deveCalcularValorIrrfPrimeiraFaixaSalarial() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("2530.00"));
    request.setDependentes(0);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("211.48"));
    inssResponse.setAliquota(7.5);

    ParametroIrrf faixaIsenta = ParametroIrrfMock.getPrimeiraFaixaSalarial();

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("2530.00")))
      .thenReturn(Optional.of(faixaIsenta));

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenReturn(inssResponse);

    IrrfResponse response = calculadoraIrrf.calcularIrrf(request);

    assertThat(response).isNotNull();
    assertThat(response.getIrrf()).isEqualTo(new BigDecimal("15.49"));
    assertThat(response.getAliquota()).isEqualTo(7.5);
  }

  @Test
  void calcularIrrf_deveCalcularValorIrrfSegundaFaixaSalarial() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("3390.15"));
    request.setDependentes(0);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("305.64"));
    inssResponse.setAliquota(7.5);

    ParametroIrrf faixaIsenta = ParametroIrrfMock.getSegundaFaixaSalarial();

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("3390.15")))
      .thenReturn(Optional.of(faixaIsenta));

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenReturn(inssResponse);

    IrrfResponse response = calculadoraIrrf.calcularIrrf(request);

    assertThat(response).isNotNull();
    assertThat(response.getIrrf()).isEqualTo(new BigDecimal("92.28"));
    assertThat(response.getAliquota()).isEqualTo(15.0);
  }

  @Test
  void calcularIrrf_deveCalcularValorIrrfTerceiraFaixaSalarial() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("4242.00"));
    request.setDependentes(0);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("412.70"));
    inssResponse.setAliquota(22.5);

    ParametroIrrf faixaIsenta = ParametroIrrfMock.getTerceiraFaixaSalarial();

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("4242.00")))
      .thenReturn(Optional.of(faixaIsenta));

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenReturn(inssResponse);

    IrrfResponse response = calculadoraIrrf.calcularIrrf(request);

    assertThat(response).isNotNull();
    assertThat(response.getIrrf()).isEqualTo(new BigDecimal("209.86"));
    assertThat(response.getAliquota()).isEqualTo(22.5);
  }

  @Test
  void calcularIrrf_deveCalcularValorIrrfQuartaFaixaSalarial() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("5410.80"));
    request.setDependentes(0);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("576.33"));
    inssResponse.setAliquota(22.5);

    ParametroIrrf faixaIsenta = ParametroIrrfMock.getQuartaFaixaSalarial();

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("5410.80")))
      .thenReturn(Optional.of(faixaIsenta));

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenReturn(inssResponse);

    IrrfResponse response = calculadoraIrrf.calcularIrrf(request);

    assertThat(response).isNotNull();
    assertThat(response.getIrrf()).isEqualTo(new BigDecimal("444.52"));
    assertThat(response.getAliquota()).isEqualTo(27.5);
  }

  @Test
  void calcularIrrf_deveCalcularValorIrrfComDependentesComSucesso() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("3140"));
    request.setDependentes(1);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("20"));
    inssResponse.setAliquota(2.0);

    ParametroIrrf primeiraFaixa = ParametroIrrfMock.getPrimeiraFaixaSalarial();

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("3140")))
      .thenReturn(Optional.of(primeiraFaixa));

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenReturn(inssResponse);

    IrrfResponse response = calculadoraIrrf.calcularIrrf(request);

    assertThat(response).isNotNull();
    assertThat(response.getIrrf()).isNotNull();
    assertThat(response.getAliquota()).isNotNull();
  }

  @Test
  void calcularIrrf_deveLancarExcecaoQuandoNaoEncontrarParametroFaixaSalarial() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("4380"));
    request.setDependentes(0);

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("4380")))
      .thenReturn(Optional.empty());

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraIrrf.calcularIrrf(request));

    String expectedMessage = "Não foi possível identificar a faixa salarial para o valor informado.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calcularIrrf_deveLancarExcecaoQuandoOcorrerErroInesperado() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("4380"));
    request.setDependentes(0);

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("4380")))
      .thenThrow(new RuntimeException("Erro inesperado."));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraIrrf.calcularIrrf(request));

    String expectedMessage = "Desculpe, não foi possível completar a solicitação.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }
}
