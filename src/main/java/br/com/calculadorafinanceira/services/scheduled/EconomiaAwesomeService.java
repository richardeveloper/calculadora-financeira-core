package br.com.calculadorafinanceira.services.scheduled;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.responses.integration.CambioResponse;
import br.com.calculadorafinanceira.services.integration.EconomiaAwesomeIntegration;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class EconomiaAwesomeService {

  private static final String DOLAR_REAL = "USD-BRL";
  private static final String EURO_REAL = "EUR-BRL";
  private static final String LIBRA_REAL = "GBP-BRL";
  private static final String BITCOIN_REAL = "BTC-BRL";

  @Autowired
  private EconomiaAwesomeIntegration economiaAwesomeIntegration;

  @Autowired
  private ObjectMapper objectMapper;

  public CambioResponse consultarCambioAtual() {
    try {
      log.info("Iniciando integração com API de Economia da Awesome.");

      Response response = economiaAwesomeIntegration.consultarCambioAtual(
        DOLAR_REAL + "," + EURO_REAL + "," +  LIBRA_REAL
      );

      CambioResponse moedaAtualResponse = objectMapper.readValue(response.body().asInputStream(), CambioResponse.class);

      log.info("Finalizando integração com API Economia da Awesome.");

      return moedaAtualResponse;
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
      throw new ServiceException("Erro ao realizar consulta do câmbio atual: " + e.getMessage());
    }
  }

}
