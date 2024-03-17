package br.com.calculadorafinanceira.utils;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeriodoUtilsTest {

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void gerarInformativoPeriodo_deveGerarInformativoDeExatamenteUmAno() {
    long periodoMeses = 12L;
    LocalDate inicioPeriodo = LocalDate.now().minusYears(1);
    LocalDate fimPeriodo = LocalDate.now();

    String info = PeriodoUtils.gerarInformativoPeriodo(periodoMeses);
    String info2 = PeriodoUtils.gerarInformativoPeriodo(inicioPeriodo, fimPeriodo);

    assertThat(info).isEqualTo("1 Ano");
    assertThat(info2).isEqualTo("1 Ano");
  }

  @Test
  void gerarInformativoPeriodo_deveGerarInformativoDeExatamenteUmMes() {
    long periodoMeses = 1L;
    LocalDate inicioPeriodo = LocalDate.now().minusMonths(1);
    LocalDate fimPeriodo = LocalDate.now();

    String info = PeriodoUtils.gerarInformativoPeriodo(periodoMeses);
    String info2 = PeriodoUtils.gerarInformativoPeriodo(inicioPeriodo, fimPeriodo);

    assertThat(info).isEqualTo("1 Mês");
    assertThat(info2).isEqualTo("1 Mês");
  }

  @Test
  void gerarInformativoPeriodo_deveGerarInformativoDeVariosAnos() {
    long periodoMeses = 120L;
    LocalDate inicioPeriodo = LocalDate.now().minusYears(10);
    LocalDate fimPeriodo = LocalDate.now();

    String info = PeriodoUtils.gerarInformativoPeriodo(periodoMeses);
    String info2 = PeriodoUtils.gerarInformativoPeriodo(inicioPeriodo, fimPeriodo);

    assertThat(info).isEqualTo("10 Anos");
    assertThat(info2).isEqualTo("10 Anos");
  }

  @Test
  void gerarInformativoPeriodo_deveGerarInformativoDeVariosMeses() {
    long periodoMeses = 11L;
    LocalDate inicioPeriodo = LocalDate.now().minusMonths(11);
    LocalDate fimPeriodo = LocalDate.now();

    String info = PeriodoUtils.gerarInformativoPeriodo(periodoMeses);
    String info2 = PeriodoUtils.gerarInformativoPeriodo(inicioPeriodo, fimPeriodo);

    assertThat(info).isEqualTo("11 Meses");
    assertThat(info2).isEqualTo("11 Meses");
  }

  @Test
  void gerarInformativoPeriodo_deveGerarInformativoDeVariosAnosEVariosMeses() {
    long periodoMeses = 66L;
    LocalDate inicioPeriodo = LocalDate.now().minusYears(5).minusMonths(6);
    LocalDate fimPeriodo = LocalDate.now();

    String info = PeriodoUtils.gerarInformativoPeriodo(periodoMeses);
    String info2 = PeriodoUtils.gerarInformativoPeriodo(inicioPeriodo, fimPeriodo);

    assertThat(info).isEqualTo("5 Anos e 6 Meses");
    assertThat(info2).isEqualTo("5 Anos e 6 Meses");
  }

  @Test
  void gerarInformativoPeriodo_deveGerarInformativoDeUmAnoEVariosMeses() {
    long periodoMeses = 21L;
    LocalDate inicioPeriodo = LocalDate.now().minusYears(1).minusMonths(9);
    LocalDate fimPeriodo = LocalDate.now();

    String info = PeriodoUtils.gerarInformativoPeriodo(periodoMeses);
    String info2 = PeriodoUtils.gerarInformativoPeriodo(inicioPeriodo, fimPeriodo);

    assertThat(info).isEqualTo("1 Ano e 9 Meses");
    assertThat(info2).isEqualTo("1 Ano e 9 Meses");
  }

  @Test
  void gerarInformativoPeriodo_deveGerarInformativoDeVarisAnosEUmMes() {
    long periodoMeses = 49L;
    LocalDate inicioPeriodo = LocalDate.now().minusYears(4).minusMonths(1);
    LocalDate fimPeriodo = LocalDate.now();

    String info = PeriodoUtils.gerarInformativoPeriodo(periodoMeses);
    String info2 = PeriodoUtils.gerarInformativoPeriodo(inicioPeriodo, fimPeriodo);

    assertThat(info).isEqualTo("4 Anos e 1 Mês");
    assertThat(info2).isEqualTo("4 Anos e 1 Mês");
  }

  @Test
  void gerarInformativoPeriodo_deveLancarExcecaoCasoPeriodoMenorQueZero() {
    long periodoMeses = -1;

    ServiceException exception = assertThrows(ServiceException.class,
      () -> PeriodoUtils.gerarInformativoPeriodo(periodoMeses));

    String expectedMessage = "O período informado em meses deve ser maior que zero.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }

  @Test
  void gerarInformativoPeriodo_deveLancarExcecaoCasoDataFimMenorQueDataInicio() {
    LocalDate inicioPeriodo = LocalDate.now();
    LocalDate fimPeriodo = LocalDate.now().minusDays(1);

    ServiceException exception = assertThrows(ServiceException.class,
      () -> PeriodoUtils.gerarInformativoPeriodo(inicioPeriodo, fimPeriodo));

    String expectedMessage = "A data de início do período deve ser inferior a data de término.";

    assertThat(expectedMessage).isEqualTo(exception.getMessage());
  }
}