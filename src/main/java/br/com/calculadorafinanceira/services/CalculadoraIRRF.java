package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroIRRF;
import br.com.calculadorafinanceira.exceptions.ServiceException;
import br.com.calculadorafinanceira.repositories.ParametroIRRFRepository;
import br.com.calculadorafinanceira.requests.INSSRequest;
import br.com.calculadorafinanceira.requests.IRRFRequest;
import br.com.calculadorafinanceira.responses.IRRFResponse;
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

  public IRRFResponse calcularIRRF(IRRFRequest request) throws ServiceException {

    BigDecimal salarioBruto = request.getSalarioBruto();

    ParametroIRRF parametroIRRF = parametroIRRFRepository
      .findBySalarioBruto(request.getSalarioBruto())
      .orElseThrow(() -> new ServiceException("Não foi possível identificar a faixa salarial para o valor informado."));

    BigDecimal descontoDependentes = BigDecimal.ZERO;

    int dependentes = request.getDependentes();

    if (dependentes > 0) {
      descontoDependentes = VALOR_DEDUCAO_DEPENDENTE.multiply(BigDecimal.valueOf(dependentes));
    }

    INSSRequest inssRequest = INSSRequest.builder()
      .salarioBruto(salarioBruto)
      .build();

    BigDecimal inss = calculadoraINSS.calcularINSS(inssRequest).getInss();

    BigDecimal baseParaCalculo = salarioBruto
      .subtract(descontoDependentes)
      .subtract(inss);

    BigDecimal irrf = baseParaCalculo
      .multiply(BigDecimal.valueOf(parametroIRRF.getAliquota() / 100))
      .subtract(parametroIRRF.getParcelaDedutivel())
      .setScale(2, RoundingMode.HALF_UP);

    return IRRFResponse.builder()
      .irrf(irrf.compareTo(BigDecimal.ZERO) > 0 ? irrf.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
      .aliquota(parametroIRRF.getAliquota())
      .build();
  }
}
