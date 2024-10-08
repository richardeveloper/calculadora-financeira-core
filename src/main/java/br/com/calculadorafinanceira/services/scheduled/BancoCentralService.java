package br.com.calculadorafinanceira.services.scheduled;

import br.com.calculadorafinanceira.entities.TaxaCdiEntity;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.repositories.TaxaCdiRepository;
import br.com.calculadorafinanceira.requests.integration.TaxaCdiRequest;
import br.com.calculadorafinanceira.responses.integration.RegistroTaxaCdi;
import br.com.calculadorafinanceira.responses.integration.TaxaCdiResponse;
import br.com.calculadorafinanceira.services.integration.BancoCentralIntegration;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Response;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BancoCentralService {

  private static final String SORT_PARAMETER = "%5B%5D";
  private static final int PAGE = 1;
  private static final int PAGE_SIZE = 20;
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  @Autowired
  private BancoCentralIntegration bancoCentralIntegration;

  @Autowired
  private TaxaCdiRepository taxaCdiRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Scheduled(cron = "0 0 1 * * 1-5")
  public void consultarTaxaCdi() throws ServiceException {

    try {
      log.info("Iniciando integração com API do Banco Central.");

      LocalDate dataConsulta = LocalDate.now().minusDays(1);

      switch (dataConsulta.getDayOfWeek()) {
        case SATURDAY -> dataConsulta = dataConsulta.minusDays(1);
        case SUNDAY -> dataConsulta = dataConsulta.minusDays(2);
      }

      String data = dataConsulta.format(DATE_FORMAT);

      TaxaCdiRequest taxaCdiRequest = TaxaCdiRequest.builder()
        .dataInicial(data)
        .dataFinal(data)
        .build();

      Response response = bancoCentralIntegration.consultarTaxaCdi(taxaCdiRequest, SORT_PARAMETER, PAGE, PAGE_SIZE);

      TaxaCdiResponse taxaCdiResponse = objectMapper.readValue(response.body().asInputStream(), TaxaCdiResponse.class);

      if (taxaCdiResponse.getObservacoes().isEmpty()) {

        RegistroTaxaCdi registroTaxaCdi = taxaCdiResponse
          .getRegistros()
          .stream()
          .findFirst()
          .orElseThrow(() -> new ServiceException("Não foi possível recuperar taxa do CDI."));

        Optional<TaxaCdiEntity> taxaCdiEntity = taxaCdiRepository.findLastRegistered();

        if (taxaCdiEntity.isEmpty() || !taxaCdiEntity.get().getValor().equals(registroTaxaCdi.getTaxaAnual())) {

          TaxaCdiEntity taxaCdi = TaxaCdiEntity.builder()
            .valor(registroTaxaCdi.getTaxaAnual())
            .dataConsulta(LocalDateTime.now())
            .build();

          taxaCdiRepository.save(taxaCdi);

          log.info("Taxa CDI cadastrada com sucesso.");
        }
      }

      log.info("Finalizando integração com API do Banco Central.");
    }
    catch (FeignException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro na comunicação com serviço de terceiros: " + e.getMessage());
    }
    catch (StreamReadException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao ler resposta da solicitação: " + e.getMessage());
    }
    catch (DatabindException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao associar dados de resposta da solicitação: " + e.getMessage());
    }
    catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao converter resposta da solicitação: " + e.getMessage());
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao consultar taxa do CDI: " + e.getMessage());
    }
  }

}