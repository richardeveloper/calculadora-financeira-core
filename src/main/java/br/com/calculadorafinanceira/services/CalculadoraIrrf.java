package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroIrrfEntity;
import br.com.calculadorafinanceira.enums.TipoPeriodo;
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
  private static final int SEIS_MESES = 6;
  private static final int DOZE_MESES = 12;
  private static final int VINTE_E_QUATRO_MESES = 24;

  @Autowired
  private CalculadoraInss calculadoraInss;

  @Autowired
  private ParametroIrrfRepository parametroIrrfRepository;

  public IrrfResponse calcularIrrf(IrrfRequest request) throws ServiceException {

    try {
      ParametroIrrfEntity parametroIrrfEntity = parametroIrrfRepository
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

      BigDecimal inss = calculadoraInss.calcularInss(inssRequest).getInss();

      BigDecimal baseParaCalculo = request.getSalarioBruto()
        .subtract(descontoDependentes)
        .subtract(inss);

      BigDecimal irrf = baseParaCalculo
        .multiply(BigDecimal.valueOf(parametroIrrfEntity.getAliquota() / PERCENTAGE_DIVISOR))
        .subtract(parametroIrrfEntity.getParcelaDedutivel())
        .setScale(2, RoundingMode.HALF_UP);

      return IrrfResponse.builder()
        .irrf(irrf.compareTo(BigDecimal.ZERO) > 0 ? irrf.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO)
        .aliquota(parametroIrrfEntity.getAliquota())
        .build();

    } catch (ServiceException e) {
      throw e;
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Desculpe, não foi possível completar a solicitação.");
    }
  }

  public IrrfResponse calcularIrrfSobreRendimento(BigDecimal valorRendimento, int tempoInvestimento,
    TipoPeriodo tipoPeriodo) {

    if (tipoPeriodo.equals(TipoPeriodo.ANUAL)) {
      tempoInvestimento = tempoInvestimento * 12;
    }

    double aliquota;

    if (tempoInvestimento < SEIS_MESES) {
      aliquota = 22.5;
    }
    else if (tempoInvestimento < DOZE_MESES) {
      aliquota = 20.0;
    }
    else if (tempoInvestimento < VINTE_E_QUATRO_MESES) {
      aliquota = 17.5;
    }
    else {
      aliquota = 15.0;
    }

    BigDecimal irrf = valorRendimento
      .multiply(BigDecimal.valueOf(aliquota / PERCENTAGE_DIVISOR))
      .setScale(2, RoundingMode.HALF_UP);

    return IrrfResponse.builder()
      .irrf(irrf)
      .aliquota(aliquota)
      .build();
  }

}
