package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.enums.TipoPeriodo;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.RendimentoCdiRequest;
import br.com.calculadorafinanceira.requests.integration.TaxaCdiRequest;
import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.responses.IrrfResponse;
import br.com.calculadorafinanceira.responses.JurosCompostosResponse;
import br.com.calculadorafinanceira.responses.RendimentoCdiResponse;
import br.com.calculadorafinanceira.requests.dto.Juros;
import br.com.calculadorafinanceira.requests.dto.Periodo;
import br.com.calculadorafinanceira.responses.integration.RegistroTaxaCdi;
import br.com.calculadorafinanceira.responses.integration.TaxaCdiResponse;
import br.com.calculadorafinanceira.services.integration.BancoCentralIntegration;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class CalculadoraRendimento {

  private static final String SORT_PARAMETER = "%5B%5D";
  private static final int PAGE = 1;
  private static final int PAGE_SIZE = 20;
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  @Autowired
  private BancoCentralIntegration bancoCentralIntegration;

  @Autowired
  private CalculadoraJuros calculadoraJuros;

  @Autowired
  private CalculadoraIrrf calculadoraIrrf;

  @Autowired
  private ObjectMapper objectMapper;

  public RendimentoCdiResponse calcularRendimentoCdi(RendimentoCdiRequest request) throws ServiceException {

    try {
      RegistroTaxaCdi registroTaxa = consultarTaxaCdi()
        .getRegistros()
        .stream()
        .findFirst()
        .orElseThrow(() -> new ServiceException("Não foi possível recuperar taxa do CDI."));

      Double taxaCdi = registroTaxa.getTaxaAnual();
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

  private TaxaCdiResponse consultarTaxaCdi() throws ServiceException {

    try {
      String dataConsulta = LocalDate.now().minusDays(1).format(DATE_FORMAT);

      TaxaCdiRequest taxaCdiRequest = TaxaCdiRequest.builder()
        .dataInicial(dataConsulta)
        .dataFinal(dataConsulta)
        .build();

      Response response = bancoCentralIntegration.consultarTaxaCdi(taxaCdiRequest, SORT_PARAMETER, PAGE, PAGE_SIZE);

      TaxaCdiResponse taxaCdiResponse = objectMapper.readValue(response.body().asInputStream(), TaxaCdiResponse.class);

      return taxaCdiResponse;
    }
    catch (FeignException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao consultar taxa CDI: " + e.getMessage());
    }
    catch (StreamReadException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao ler response da requisição: ", e);
    }
    catch (DatabindException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao converter response da requisição: ");
    }
    catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao ler response da requisição: ");
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao calcular rendimento do CDI: ");
    }
  }

}
