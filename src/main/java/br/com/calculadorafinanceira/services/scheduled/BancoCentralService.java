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
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

  /**
   *  EXECUTAR SERVIÇO CASO NÃO TENHA REGISTRO NO BANCO DE DADOS
   */
  @PostConstruct
  public void init() {
    consultarTaxaCdi();
  }

  /**
   *  SERVIÇO EXECUTADO DE SEGUNDA A SEXTA A 1 HORA DA MANHÃ
   */
  @Scheduled(cron = "0 0 1 * * 1-5")
  public void consultarTaxaCdi() throws ServiceException {

    try {
      log.info("Iniciando integração com sistema do Banco Central");

      /**
       *  API DO BANCO CENTRAL REQUER DATA INFERIOR A ATUAL
       */
      LocalDate dataConsulta = LocalDate.now().minusDays(1);

      if (dataConsulta.getDayOfWeek().equals(DayOfWeek.SATURDAY) || dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
        switch (dataConsulta.getDayOfWeek()) {
          case SATURDAY -> dataConsulta = dataConsulta.minusDays(1);
          case SUNDAY -> dataConsulta = dataConsulta.minusDays(2);
        }
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

          log.info("Valor de Taxa CDI cadastrada com sucesso.");
        }
      }

      log.info("Finalizando integração com sistema do Banco Central");
    }
    catch (FeignException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao consultar taxa CDI: " + e.getMessage());
    }
    catch (StreamReadException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao ler response da requisição: " + e.getMessage());
    }
    catch (DatabindException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao converter response da requisição: " + e.getMessage());
    }
    catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao ler response da requisição: " + e.getMessage());
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Erro ao calcular rendimento do CDI: " + e.getMessage());
    }
  }

}