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

public class CalculadoraIrrfTest {

  @Mock
  ParametroIrrfRepository parametroIrrfRepository;

  @Mock
  CalculadoraInss calculadoraInss;

  @InjectMocks
  CalculadoraIrrf calculadoraIrrf;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void calcularIrrf_deveCalcularIrrfZeroQuandoSalarioIgualZero() {

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
  public void calcularIrrf_deveCalcularIrrfSemDependentesComSucesso() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("2690"));
    request.setDependentes(0);

    InssResponse inssResponse = new InssResponse();
    inssResponse.setInss(new BigDecimal("10"));
    inssResponse.setAliquota(1.0);

    ParametroIrrf faixaIsenta = ParametroIrrfMock.getFaixaIsenta();

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("2690")))
      .thenReturn(Optional.of(faixaIsenta));

    when(calculadoraInss.calcularInss(any(InssRequest.class)))
      .thenReturn(inssResponse);

    IrrfResponse response = calculadoraIrrf.calcularIrrf(request);

    assertThat(response).isNotNull();
    assertThat(response.getIrrf()).isNotNull();
    assertThat(response.getAliquota()).isNotNull();
  }

  @Test
  public void calcularIrrf_deveCalcularIrrfComDependentesComSucesso() {

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
  public void calcularIrrf_deveLancarExecaoQuandoNaoEncontrarParametroFaixaSalarial() {

    IrrfRequest request = new IrrfRequest();
    request.setSalarioBruto(new BigDecimal("4380"));
    request.setDependentes(0);

    when(parametroIrrfRepository.findBySalarioBruto(new BigDecimal("4380")))
      .thenReturn(Optional.empty());

    ServiceException exception = assertThrows(ServiceException.class, () -> calculadoraIrrf.calcularIrrf(request));

    String expectedMessage = "Não foi possível identificar a faixa salarial para o valor informado.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }
}
