package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroInss;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CalculadoraInssTest {

  @Mock
  ParametroInssRepository parametroInssRepository;

  @InjectMocks
  CalculadoraInss calculadoraInss;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void calularInss_deveRetornarInssZeroQuandoSalarioIgualZero() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(BigDecimal.ZERO);

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response.getInss()).isEqualTo(BigDecimal.ZERO);
    assertThat(response.getAliquota()).isEqualTo(0.0);
  }

  @Test
  public void calularInss_deveCalcularInssPrimeiraFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("1280.00"));

    ParametroInss primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(primeiraFaixa));

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("96.00"));
    assertThat(response.getAliquota()).isEqualTo(7.5);
  }

  @Test
  public void calularInss_deveLancarExcecaoQuandoNaoEncontrarPrimeiraFaixaInss() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("1280.00"));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.empty());

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Não foi possível recuperar informações da primeira faixa salarial.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  public void calularInss_deveCalcularInssSegundaFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("2360.00"));

    ParametroInss primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    ParametroInss segundaFaixa = ParametroInssMock.getSegundaFaixaSalarial();

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(primeiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(segundaFaixa));

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("191.22"));
    assertThat(response.getAliquota()).isEqualTo(9.0);
  }

  @Test
  public void calularInss_deveLancarExcecaoQuandoNaoEncontrarParametroSegundaFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("2360.00"));

    ParametroInss primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(primeiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL))
      .thenReturn(Optional.empty());

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Não foi possível recuperar informações da segunda faixa salarial.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  public void calularInss_deveCalcularInssTerceiraFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("3000.00"));

    ParametroInss primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    ParametroInss segundaFaixa = ParametroInssMock.getSegundaFaixaSalarial();

    ParametroInss terceiraFaixa = ParametroInssMock.getTerceiraFaixaSalarial();

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(primeiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(segundaFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(terceiraFaixa));

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("258.82"));
    assertThat(response.getAliquota()).isEqualTo(12.0);
  }

  @Test
  public void calularInss_deveLancarExcecaoQuandoNaoEncontrarParametroTerceiraFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("3000.00"));

    ParametroInss primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    ParametroInss segundaFaixa = ParametroInssMock.getSegundaFaixaSalarial();

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(primeiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(segundaFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.empty());

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Não foi possível recuperar informações da terceira faixa salarial.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  public void calularInss_deveCalcularInssQuartaFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("5164.09"));

    ParametroInss primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    ParametroInss segundaFaixa = ParametroInssMock.getSegundaFaixaSalarial();

    ParametroInss terceiraFaixa = ParametroInssMock.getTerceiraFaixaSalarial();

    ParametroInss quartaFaixa = ParametroInssMock.getQuartaFaixaSalarial();

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(primeiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(segundaFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(terceiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(quartaFaixa));

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("541.79"));
    assertThat(response.getAliquota()).isEqualTo(14.0);
  }

  @Test
  public void calularInss_deveLancarExcecaoQuandoNaoEncontrarParametroQuartaFaixaSalarial() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("5164.09"));

    ParametroInss primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    ParametroInss segundaFaixa = ParametroInssMock.getSegundaFaixaSalarial();

    ParametroInss terceiraFaixa = ParametroInssMock.getTerceiraFaixaSalarial();


    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(primeiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(segundaFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(terceiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL))
      .thenReturn(Optional.empty());

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Não foi possível recuperar informações da quarta faixa salarial.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  public void calularInss_deveCalcularTetoInss() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("7796.02"));

    ParametroInss primeiraFaixa = ParametroInssMock.getPrimeiraFaixaSalarial();

    ParametroInss segundaFaixa = ParametroInssMock.getSegundaFaixaSalarial();

    ParametroInss terceiraFaixa = ParametroInssMock.getTerceiraFaixaSalarial();

    ParametroInss quartaFaixa = ParametroInssMock.getQuartaFaixaSalarial();

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(primeiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(segundaFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(terceiraFaixa));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL))
      .thenReturn(Optional.of(quartaFaixa));

    InssResponse response = calculadoraInss.calcularInss(request);

    assertThat(response).isNotNull();
    assertThat(response.getInss()).isEqualTo(new BigDecimal("908.86"));
    assertThat(response.getAliquota()).isEqualTo(14.0);
  }

  @Test
  public void calcularInss_deveLancarExcecaoQuandoOcorrerErroInesperado() {

    InssRequest request = new InssRequest();
    request.setSalarioBruto(new BigDecimal("4380.00"));

    when(parametroInssRepository.findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
      .thenThrow(new RuntimeException("Erro inesperado."));

    ServiceException exception = Assert.assertThrows(ServiceException.class,
      () -> calculadoraInss.calcularInss(request));

    String expectedMessage = "Desculpe, não foi possível completar a solicitação.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

}