package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroIRRF;
import br.com.calculadorafinanceira.exceptions.ServiceException;
import br.com.calculadorafinanceira.exceptions.ValidationException;
import br.com.calculadorafinanceira.repositories.ParametroIRRFRepository;
import br.com.calculadorafinanceira.views.IRRFView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculadoraIRRF {
  private static final BigDecimal VALOR_DEDUCAO_DEPENDENTE = new BigDecimal("189.59");

  @Autowired
  private CalculadoraINSS calculadoraINSS;

  @Autowired
  private ParametroIRRFRepository parametroIRRFRepository;

  public IRRFView calcularIRRF(BigDecimal salarioBruto, Integer numeroDependentes) throws ServiceException {
    validarParametros(salarioBruto, numeroDependentes);

    ParametroIRRF parametroIRRF = parametroIRRFRepository
      .findBySalarioBruto(salarioBruto)
      .orElseThrow(() -> new ServiceException("Não foi possível identificar a faixa salarial para o valor informado."));

    BigDecimal descontoDependentes = BigDecimal.ZERO;

    if (numeroDependentes > 0) {
      descontoDependentes = VALOR_DEDUCAO_DEPENDENTE.multiply(BigDecimal.valueOf(numeroDependentes));
    }

    BigDecimal INSS = calculadoraINSS.calcularINSS(salarioBruto).getInss();

    BigDecimal baseParaCalculo = salarioBruto
      .subtract(descontoDependentes)
      .subtract(INSS);

    BigDecimal IRRF = baseParaCalculo
      .multiply(BigDecimal.valueOf(parametroIRRF.getAliquota() / 100))
      .subtract(parametroIRRF.getParcelaDedutivel())
      .setScale(2, RoundingMode.HALF_UP);

    return IRRFView.builder()
      .irrf(IRRF.compareTo(BigDecimal.ZERO) > 0 ? IRRF.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
      .aliquota(parametroIRRF.getAliquota())
      .build();
  }

  private void validarParametros(BigDecimal salarioBruto, Integer numeroDependentes) throws ValidationException {
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
  }
}
