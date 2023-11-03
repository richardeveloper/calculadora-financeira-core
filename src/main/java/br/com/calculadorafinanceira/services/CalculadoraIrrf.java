package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroIrrf;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.repositories.ParametroIrrfRepository;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.responses.IrrfResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculadoraIrrf {
  private static final Double PERCENTAGE_DIVISOR = 100.00;
  private static final BigDecimal VALOR_DEDUCAO_DEPENDENTE = new BigDecimal("189.59");

  @Autowired
  private CalculadoraInss calculadoraINSS;

  @Autowired
  private ParametroIrrfRepository parametroIRRFRepository;

  public IrrfResponse calcularIRRF(IrrfRequest request) throws ServiceException {

    try {

      ParametroIrrf parametroIRRF = parametroIRRFRepository
        .findBySalarioBruto(request.getSalarioBruto())
        .orElseThrow(() -> new ServiceException("Não foi possível identificar a faixa salarial para o valor informado."));

      BigDecimal descontoDependentes = BigDecimal.ZERO;

      int dependentes = request.getDependentes();

      if (dependentes > 0) {
        descontoDependentes = VALOR_DEDUCAO_DEPENDENTE.multiply(BigDecimal.valueOf(dependentes));
      }

      InssRequest inssRequest = InssRequest.builder()
        .salarioBruto(request.getSalarioBruto())
        .build();

      BigDecimal inss = calculadoraINSS.calcularINSS(inssRequest).getInss();

      BigDecimal baseParaCalculo = request.getSalarioBruto()
        .subtract(descontoDependentes)
        .subtract(inss);

      BigDecimal irrf = baseParaCalculo
        .multiply(BigDecimal.valueOf(parametroIRRF.getAliquota() / PERCENTAGE_DIVISOR))
        .subtract(parametroIRRF.getParcelaDedutivel())
        .setScale(2, RoundingMode.HALF_UP);

      return IrrfResponse.builder()
        .irrf(irrf.compareTo(BigDecimal.ZERO) > 0 ? irrf.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
        .aliquota(parametroIRRF.getAliquota())
        .build();

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}
