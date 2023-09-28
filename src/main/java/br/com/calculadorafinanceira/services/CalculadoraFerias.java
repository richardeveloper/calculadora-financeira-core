package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.exceptions.ValidationException;
import br.com.calculadorafinanceira.views.FeriasView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculadoraFerias {

  @Autowired
  private CalculadoraIRRF calculadoraIRRF;

  @Autowired
  private CalculadoraINSS calculadoraINSS;

  private static final Integer DAYS_OF_MONTH = 30;
  private static final Integer PRECISION_SCALE = 10;

  public FeriasView calcularFerias(BigDecimal salarioBruto, Integer numeroDependentes,
    boolean abonoPecuniario, Integer diasFerias) {

    validarParametros(salarioBruto, numeroDependentes, abonoPecuniario, diasFerias);

    BigDecimal saldoFerias = salarioBruto
      .divide(BigDecimal.valueOf(DAYS_OF_MONTH), PRECISION_SCALE, RoundingMode.HALF_UP)
      .multiply(BigDecimal.valueOf(diasFerias)).setScale(2, RoundingMode.HALF_UP);

    BigDecimal tercoFerias = saldoFerias
      .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);

    BigDecimal baseParaCalculoImpostos = saldoFerias.add(tercoFerias);

    BigDecimal valorAbonoPecuniario = BigDecimal.ZERO;
    BigDecimal tercoAbonoPecuniario = BigDecimal.ZERO;

    if (abonoPecuniario) {
      BigDecimal diasVendidos = BigDecimal.valueOf(DAYS_OF_MONTH).subtract(BigDecimal.valueOf(diasFerias));

      valorAbonoPecuniario = salarioBruto
        .divide(BigDecimal.valueOf(DAYS_OF_MONTH), PRECISION_SCALE, RoundingMode.HALF_UP)
        .multiply(diasVendidos).setScale(2, RoundingMode.HALF_UP);

      tercoAbonoPecuniario = valorAbonoPecuniario.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
    }

    BigDecimal inss = calculadoraINSS
      .calcularINSS(baseParaCalculoImpostos)
      .getInss();

    BigDecimal irrf = calculadoraIRRF
      .calcularIRRF(baseParaCalculoImpostos, numeroDependentes)
      .getIrrf();

    BigDecimal totalFerias = saldoFerias
      .add(tercoFerias)
      .add(valorAbonoPecuniario)
      .add(tercoAbonoPecuniario)
      .subtract(inss)
      .subtract(irrf);

    return FeriasView.builder()
      .saldoFerias(saldoFerias)
      .tercoFerias(tercoFerias)
      .abonoPecuniario(valorAbonoPecuniario)
      .tercoAbonoPecuniario(tercoAbonoPecuniario)
      .descontoInss(inss)
      .descontoIrrf(irrf)
      .totalFerias(totalFerias)
      .build();
  }

  private void validarParametros(BigDecimal salarioBruto, Integer numeroDependentes,
    boolean abonoPecuniario, Integer diasFerias) {

    if (salarioBruto == null) {
      throw new ValidationException("O campo salarioBruto é obrigatório.");
    }

    if (salarioBruto.compareTo(BigDecimal.ZERO) <= 0) {
      throw new ValidationException("O campo salarioBruto deve ser positivo.");
    }

    if (numeroDependentes == null) {
      throw new ValidationException("O campo numeroDependentes é obrigatório.");
    }

    if (numeroDependentes < 0) {
      throw new ValidationException("O campo numeroDependentes deve ser positivo.");
    }

    if (numeroDependentes > 10) {
      throw new ValidationException("O campo numeroDependentes não pode ser superior a 10.");
    }

    if (diasFerias <= 0) {
      throw new ValidationException("O campo diasFerias deve maior que zero.");
    }

    if (diasFerias > DAYS_OF_MONTH) {
      throw new ValidationException("O campo diasFerias deve ser menor ou igual o total de dias do mês.");
    }

    if (abonoPecuniario && diasFerias > 20) {
      throw new ValidationException("Ao solicitar o abono pecuniário, a quantidade máxima permitida é de 20 dias de férias.");
    }
  }
}
