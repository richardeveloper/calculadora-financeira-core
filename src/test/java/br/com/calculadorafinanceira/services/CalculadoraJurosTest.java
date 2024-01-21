package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.enums.TipoPeriodo;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.Juros;
import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.requests.JurosSimplesRequest;
import br.com.calculadorafinanceira.requests.Periodo;
import br.com.calculadorafinanceira.responses.JurosCompostosResponse;
import br.com.calculadorafinanceira.responses.JurosSimplesResponse;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

class CalculadoraJurosTest {

  private static final BigDecimal VARIACAO_PERMITIDA = new BigDecimal("0.02");

  private CalculadoraJuros calculadoraJuros;

  @BeforeEach
  public void setUp() {
    this.calculadoraJuros = new CalculadoraJuros();
  }

  /**
   * JUROS SIMPLES
   */
  @Test
  void calcularJurosSimples_deveCalcularJurosSimplesMensalComPeriodoMensalComSucesso() {

    JurosSimplesRequest request = JurosSimplesRequest.builder()
      .valorInicial(new BigDecimal("1000.00"))
      .juros(new Juros(TipoPeriodo.MENSAL, new BigDecimal("1")))
      .periodo(new Periodo(TipoPeriodo.MENSAL, 5))
      .build();

    JurosSimplesResponse response = calculadoraJuros.calcularJurosSimples(request);

    assertThat(response.getValorInvestido()).isEqualTo(new BigDecimal("1000.00"));
    assertThatAmountWithVariation(response.getTotalJuros(), new BigDecimal("50.00"));
    assertThatAmountWithVariation(response.getValorCorrigido(), new BigDecimal("1050.00"));
  }

  @Test
  void calcularJurosSimples_deveCalcularJurosSimplesAnualComPeriodoMensalComSucesso() {

    JurosSimplesRequest request = JurosSimplesRequest.builder()
      .valorInicial(new BigDecimal("1000.00"))
      .juros(new Juros(TipoPeriodo.ANUAL, new BigDecimal("1")))
      .periodo(new Periodo(TipoPeriodo.MENSAL, 5))
      .build();

    JurosSimplesResponse response = calculadoraJuros.calcularJurosSimples(request);

    assertThat(response.getValorInvestido()).isEqualTo(new BigDecimal("1000.00"));
    assertThatAmountWithVariation(response.getTotalJuros(), new BigDecimal("4.15"));
    assertThatAmountWithVariation(response.getValorCorrigido(), new BigDecimal("1004.15"));
  }

  @Test
  void calcularJurosSimples_deveCalcularJurosSimplesMensalComPeriodoAnualComSucesso() {

    JurosSimplesRequest request = JurosSimplesRequest.builder()
      .valorInicial(new BigDecimal("1000.00"))
      .juros(new Juros(TipoPeriodo.MENSAL, new BigDecimal("1")))
      .periodo(new Periodo(TipoPeriodo.ANUAL, 5))
      .build();

    JurosSimplesResponse response = calculadoraJuros.calcularJurosSimples(request);

    assertThat(response.getValorInvestido()).isEqualTo(new BigDecimal("1000.00"));
    assertThatAmountWithVariation(response.getTotalJuros(), new BigDecimal("600.00"));
    assertThatAmountWithVariation(response.getValorCorrigido(), new BigDecimal("1600.00"));
  }

  @Test
  void calcularJurosSimples_deveCalcularJurosSimplesAnualComPeriodoAnualComSucesso() {

    JurosSimplesRequest request = JurosSimplesRequest.builder()
      .valorInicial(new BigDecimal("1000.00"))
      .juros(new Juros(TipoPeriodo.ANUAL, new BigDecimal("1")))
      .periodo(new Periodo(TipoPeriodo.ANUAL, 1))
      .build();

    JurosSimplesResponse response = calculadoraJuros.calcularJurosSimples(request);

    assertThat(response.getValorInvestido()).isEqualTo(new BigDecimal("1000.00"));
    assertThatAmountWithVariation(response.getTotalJuros(), new BigDecimal("10.00"));
    assertThatAmountWithVariation(response.getValorCorrigido(), new BigDecimal("1010.00"));
  }

  @Test
  void calcularJurosSimples_deveLancarExecaoQuandoNaoEncontrarTipoJurosSimples() {

    JurosSimplesRequest request = JurosSimplesRequest.builder()
      .valorInicial(new BigDecimal("1000.00"))
      .juros(new Juros(null, null))
      .periodo(new Periodo(TipoPeriodo.ANUAL, 5))
      .build();

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraJuros.calcularJurosSimples(request));

    String expectedMessage = "Não foi possível calcular o valor dos juros simples.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  /**
   * JUROS COMPOSTOS
   */
  @Test
  void calcularJurosComposto_deveCalcularJurosCompostoAnualComPeriodoAnualComSucesso() {

    JurosCompostosRequest request = JurosCompostosRequest.builder()
      .valorInicial(new BigDecimal("1000.00"))
      .valorMensal(new BigDecimal("240.00"))
      .juros(new Juros(TipoPeriodo.ANUAL, new BigDecimal("3")))
      .periodo(new Periodo(TipoPeriodo.ANUAL, 3))
      .build();

    JurosCompostosResponse response = calculadoraJuros.calcularJurosCompostos(request);

    assertThat(response.getValorInvestido()).isEqualTo(new BigDecimal("9640.00"));
    assertThatAmountWithVariation(response.getTotalJuros(), new BigDecimal("476.27"));
    assertThatAmountWithVariation(response.getValorCorrigido(), new BigDecimal("10116.27"));
  }

  @Test
  void calcularJurosComposto_deveCalcularJurosCompostoAnualComPeriodoMensalComSucesso() {

    JurosCompostosRequest request = JurosCompostosRequest.builder()
      .valorInicial(BigDecimal.ZERO)
      .valorMensal(new BigDecimal("100.00"))
      .juros(new Juros(TipoPeriodo.ANUAL, new BigDecimal("25")))
      .periodo(new Periodo(TipoPeriodo.MENSAL, 5))
      .build();

    JurosCompostosResponse response = calculadoraJuros.calcularJurosCompostos(request);

    assertThat(response.getValorInvestido()).isEqualTo(new BigDecimal("500.00"));
    assertThatAmountWithVariation(response.getTotalJuros(), new BigDecimal("19.12"));
    assertThatAmountWithVariation(response.getValorCorrigido(), new BigDecimal("519.12"));
  }

  @Test
  void calcularJurosComposto_deveCalcularJurosCompostoMensalComPeriodoAnualComSucesso() {

    JurosCompostosRequest request = JurosCompostosRequest.builder()
      .valorInicial(BigDecimal.ZERO)
      .valorMensal(new BigDecimal("500.00"))
      .juros(new Juros(TipoPeriodo.MENSAL, new BigDecimal("1")))
      .periodo(new Periodo(TipoPeriodo.ANUAL, 1))
      .build();

    JurosCompostosResponse response = calculadoraJuros.calcularJurosCompostos(request);

    assertThat(response.getValorInvestido()).isEqualTo(new BigDecimal("6000.00"));
    assertThatAmountWithVariation(response.getTotalJuros(), new BigDecimal("341.25"));
    assertThatAmountWithVariation(response.getValorCorrigido(), new BigDecimal("6341.25"));
  }

  @Test
  void calcularJurosComposto_deveCalcularJurosCompostoMensalComPeriodoMensalComSucesso() {

    JurosCompostosRequest request = JurosCompostosRequest.builder()
      .valorInicial(new BigDecimal("10000.00"))
      .valorMensal(BigDecimal.ZERO)
      .juros(new Juros(TipoPeriodo.MENSAL, new BigDecimal("0.5")))
      .periodo(new Periodo(TipoPeriodo.MENSAL, 60))
      .build();

    JurosCompostosResponse response = calculadoraJuros.calcularJurosCompostos(request);

    assertThat(response.getValorInvestido()).isEqualTo(new BigDecimal("10000.00"));
    assertThatAmountWithVariation(response.getTotalJuros(), new BigDecimal("3488.49"));
    assertThatAmountWithVariation(response.getValorCorrigido(), new BigDecimal("13488.49"));
  }

  @Test
  void calcularJurosSimples_deveLancarExecaoQuandoNaoEncontrarTipoJurosCompostos() {

    JurosCompostosRequest request = JurosCompostosRequest.builder()
      .valorInicial(new BigDecimal("1000.00"))
      .juros(new Juros(null, null))
      .periodo(new Periodo(TipoPeriodo.ANUAL, 5))
      .build();

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraJuros.calcularJurosCompostos(request));

    String expectedMessage = "Não foi possível calcular o valor dos juros compostos.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void calcularJurosSimples_deveLancarExecaoQuandoNaoEncontrarTipoPeriodoJurosCompostos() {

    JurosCompostosRequest request = JurosCompostosRequest.builder()
      .valorInicial(new BigDecimal("1000.00"))
//      .juros(new Juros(null, null))
      .juros(null)
      .periodo(new Periodo(TipoPeriodo.ANUAL, 5))
      .build();

    ServiceException exception = assertThrows(ServiceException.class,
      () -> calculadoraJuros.calcularJurosCompostos(request));

    String expectedMessage = "Não foi possível calcular o valor dos juros compostos.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  private AbstractBigDecimalAssert<?> assertThatAmountWithVariation(BigDecimal expected, BigDecimal actual) {
    return assertThat(expected).isBetween(actual.subtract(VARIACAO_PERMITIDA), actual.add(VARIACAO_PERMITIDA));
  }

}