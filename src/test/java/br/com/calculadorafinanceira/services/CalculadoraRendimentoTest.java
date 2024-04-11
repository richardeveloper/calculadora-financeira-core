package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.TaxaCdiEntity;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.repositories.TaxaCdiRepository;
import br.com.calculadorafinanceira.requests.RendimentoCdiRequest;
import br.com.calculadorafinanceira.responses.RendimentoCdiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class CalculadoraRendimentoTest {

  @Spy
  CalculadoraJuros calculadoraJuros;

  @Spy
  CalculadoraIrrf calculadoraIrrf;

  @Mock
  TaxaCdiRepository taxaCdiRepository;

  @InjectMocks
  CalculadoraRendimento calculadoraRendimento;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void calcularRendimentoCdi_deveCalcularRendimnentoCdiComSuscesso() {

    RendimentoCdiRequest request = new RendimentoCdiRequest();
    request.setValorAplicado(new BigDecimal("1000.00"));
    request.setTaxaRendimento(100.0);
    request.setDataInicial(LocalDate.now().minusMonths(1));
    request.setDataFinal(LocalDate.now());

    TaxaCdiEntity taxaCdi = new TaxaCdiEntity();
    taxaCdi.setId(1L);
    taxaCdi.setValor(10.0);
    taxaCdi.setDataConsulta(LocalDateTime.now());

    when(taxaCdiRepository.findLastRegistered()).thenReturn(Optional.of(taxaCdi));

    RendimentoCdiResponse response = calculadoraRendimento.calcularRendimentoCdi(request);

    assertNotNull(response);

    assertThat(response.getValorAplicado()).isEqualTo(new BigDecimal("1000.00"));
    assertThat(response.getJuros()).isEqualTo(new BigDecimal("7.97"));
    assertThat(response.getPeriodo()).isEqualTo("1 Mês");
    assertThat(response.getDescontoIrrf()).isEqualTo(new BigDecimal("1.79"));
    assertThat(response.getValorCorrigido()).isEqualTo(new BigDecimal("1006.18"));
  }

  @Test
  void calcularRendimentoCdi_deveLancarExcecaoQuandoDataInicialMaiorQueDataFinal() {

    RendimentoCdiRequest request = new RendimentoCdiRequest();
    request.setValorAplicado(new BigDecimal("1000.00"));
    request.setTaxaRendimento(100.0);
    request.setDataInicial(LocalDate.now());
    request.setDataFinal(LocalDate.now().minusMonths(6));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraRendimento.calcularRendimentoCdi(request));

    String expectedMessage = "A data final deve ser maior que a data inicial";

    assertThat(exception.getMessage()).isEqualTo(expectedMessage);
  }

  @Test
  void calcularRendimentoCdi_deveLancarExcecaoQuandoNaoEncontrarTaxaCdi() {

    RendimentoCdiRequest request = new RendimentoCdiRequest();
    request.setValorAplicado(new BigDecimal("1000.00"));
    request.setTaxaRendimento(100.0);
    request.setDataInicial(LocalDate.now().minusMonths(3));
    request.setDataFinal(LocalDate.now());

    when(taxaCdiRepository.findLastRegistered()).thenReturn(Optional.empty());

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraRendimento.calcularRendimentoCdi(request));

    String expectedMessage = "Não foi encontrado registro de taxa de cdi.";

    assertThat(exception.getMessage()).isEqualTo(expectedMessage);
  }

  @Test
  void calcularRendimentoCdi_deveLancarExcecaoQuandoOcorrerErroInesperado() {

    RendimentoCdiRequest request = new RendimentoCdiRequest();
    request.setValorAplicado(new BigDecimal("1000.00"));
    request.setTaxaRendimento(100.0);
    request.setDataInicial(LocalDate.now());
    request.setDataFinal(LocalDate.now());

    when(taxaCdiRepository.findLastRegistered()).thenThrow(new RuntimeException("Erro inesperado."));

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraRendimento.calcularRendimentoCdi(request));

    String expectedMessage = "Desculpe, não foi possível completar a solicitação.";

    assertThat(exception.getMessage()).isEqualTo(expectedMessage);
  }

}