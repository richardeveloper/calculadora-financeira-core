package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroInssEntity;
import br.com.calculadorafinanceira.enums.FaixaSalarialInss;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.mocks.ParametroInssMock;
import br.com.calculadorafinanceira.repositories.ParametroInssRepository;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.responses.InssResponse;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CalculadoraInssTest {

  @Mock
  ParametroInssRepository parametroInssRepository;

  @InjectMocks
  CalculadoraInss calculadoraInss;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void calularInss_deveRetornarInssZeroQuandoSalarioIgualZero() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(BigDecimal.ZERO);

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response.getInss()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getAliquota()).isEqualTo(0.0);
  }

  @Test
  void calularInss_deveCalcularInssPrimeiraFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("5.00"));

    List<ParametroInssEntity> parametroInssList = ParametroInssMock.getAllParametroInss();

    when(parametroInssRepository.findAll()).thenReturn(parametroInssList);

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("0.05"));
    assertThat(response.getAliquota()).isEqualTo(1.0);
  }

  @Test
  void calularInss_deveLancarExcecaoQuandoNaoEncontrarParametros() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("5.00"));

    when(parametroInssRepository.findAll()).thenReturn(List.of());

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Não foi possível recuperar os dados para cálculo do INSS.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calularInss_deveLancarExcecaoQuandoNaoEncontrarPrimeiraFaixaInss() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("5.00"));

    List<ParametroInssEntity> parametroInssList = ParametroInssMock.getAllParametroInss()
      .stream()
      .filter(e -> !e.getFaixaSalarial().equals(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .toList();

    when(parametroInssRepository.findAll()).thenReturn(parametroInssList);

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Não foi possível recuperar os dados da primeira faixa salarial.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calularInss_deveCalcularInssSegundaFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("15.00"));

    List<ParametroInssEntity> parametroInssList = ParametroInssMock.getAllParametroInss();

    when(parametroInssRepository.findAll()).thenReturn(parametroInssList);

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("0.20"));
    assertThat(response.getAliquota()).isEqualTo(2.0);
  }

  @Test
  void calularInss_deveLancarExcecaoQuandoNaoEncontrarParametroSegundaFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("15.00"));

    ParametroInssEntity primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    when(parametroInssRepository.findAll()).thenReturn(List.of(primeiraFaixa));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Não foi possível recuperar os dados da segunda faixa salarial.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calularInss_deveCalcularInssTerceiraFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("25.00"));

    List<ParametroInssEntity> parametroInssList = ParametroInssMock.getAllParametroInss();

    when(parametroInssRepository.findAll()).thenReturn(parametroInssList);

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("0.45"));
    assertThat(response.getAliquota()).isEqualTo(3.0);
  }

  @Test
  void calularInss_deveLancarExcecaoQuandoNaoEncontrarParametroTerceiraFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("25.00"));

    ParametroInssEntity primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    ParametroInssEntity segundaFaixa = ParametroInssMock.getSegundaFaixaSalarial();

    when(parametroInssRepository.findAll()).thenReturn(List.of(primeiraFaixa, segundaFaixa));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Não foi possível recuperar os dados da terceira faixa salarial.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calularInss_deveCalcularInssQuartaFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("35.00"));

    List<ParametroInssEntity> parametroInssList = ParametroInssMock.getAllParametroInss();

    when(parametroInssRepository.findAll()).thenReturn(parametroInssList);

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("0.80"));
    assertThat(response.getAliquota()).isEqualTo(4.0);
  }

  @Test
  void calularInss_deveLancarExcecaoQuandoNaoEncontrarParametroQuartaFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("35.00"));

    ParametroInssEntity primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    ParametroInssEntity segundaFaixa = ParametroInssMock.getSegundaFaixaSalarial();

    ParametroInssEntity terceiraFaixa = ParametroInssMock.getTerceiraFaixaSalarial();

    when(parametroInssRepository.findAll()).thenReturn(List.of(primeiraFaixa, segundaFaixa, terceiraFaixa));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Não foi possível recuperar os dados da quarta faixa salarial.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calularInss_deveCalcularTetoInss() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("45.00"));

    List<ParametroInssEntity> parametroInssList = ParametroInssMock.getAllParametroInss();

    when(parametroInssRepository.findAll()).thenReturn(parametroInssList);

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("1.00"));
    assertThat(response.getAliquota()).isEqualTo(4.0);
  }

  @Test
  void calcularInss_deveLancarExcecaoQuandoOcorrerErroInesperado() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("40.00"));

    when(parametroInssRepository.findAll()).thenThrow(new RuntimeException("Erro inesperado."));

    ServiceException exception = Assert.assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Desculpe, não foi possível completar a solicitação.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

}