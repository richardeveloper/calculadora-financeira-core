package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.TaxaCdiEntity;
import br.com.calculadorafinanceira.enums.TipoPeriodo;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.repositories.TaxaCdiRepository;
import br.com.calculadorafinanceira.requests.RendimentoCdiRequest;
import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.responses.IrrfResponse;
import br.com.calculadorafinanceira.responses.JurosCompostosResponse;
import br.com.calculadorafinanceira.responses.RendimentoCdiResponse;
import br.com.calculadorafinanceira.requests.dto.Juros;
import br.com.calculadorafinanceira.requests.dto.Periodo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class CalculadoraRendimento {

  @Autowired
  private TaxaCdiRepository taxaCdiRepository;

  @Autowired
  private CalculadoraJuros calculadoraJuros;

  @Autowired
  private CalculadoraIrrf calculadoraIrrf;

  public RendimentoCdiResponse calcularRendimentoCdi(RendimentoCdiRequest request) throws ServiceException {

    try {
      TaxaCdiEntity taxaCdiEntity = taxaCdiRepository.findLastRegistered()
        .orElseThrow(() -> new ServiceException("Não foi encontrado registro de taxa de cdi."));

      Double taxaCdi = taxaCdiEntity.getValor();
      Double rendimentoCdi = request.getTaxaRendimento() / 100;

      Double taxaJuros = rendimentoCdi * taxaCdi;

      Long tempoInvestimento = ChronoUnit.MONTHS.between(request.getDataInicial(), request.getDataFinal());

      JurosCompostosRequest jurosCompostosRequest = JurosCompostosRequest.builder()
        .valorAplicado(request.getValorAplicado())
        .depositoMensal(BigDecimal.ZERO)
        .juros(new Juros(TipoPeriodo.ANUAL, taxaJuros))
        .periodo(new Periodo(TipoPeriodo.MENSAL, tempoInvestimento.intValue()))
        .build();

      JurosCompostosResponse jurosCompostosResponse = calculadoraJuros.calcularJurosCompostos(
        jurosCompostosRequest);

      IrrfResponse irrfResponse = calculadoraIrrf.calcularIrrfSobreRendimento(
        jurosCompostosResponse.getTotalJuros(), tempoInvestimento.intValue(), TipoPeriodo.MENSAL);

      return RendimentoCdiResponse.builder()
        .valorAplicado(request.getValorAplicado())
        .juros(jurosCompostosResponse.getTotalJuros())
        .descontoIrrf(irrfResponse.getIrrf())
        .valorCorrigido(jurosCompostosResponse.getValorCorrigido().subtract(irrfResponse.getIrrf()))
        .build();
    }
    catch (ServiceException e) {
      throw e;
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Desculpe, não foi possível completar a solicitação.");
    }
  }

}
