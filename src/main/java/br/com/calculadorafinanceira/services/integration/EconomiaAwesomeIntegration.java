package br.com.calculadorafinanceira.services.integration;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "economia-awesome-api", url = "${economia-awesome-api.url}")
public interface EconomiaAwesomeIntegration {

  @GetMapping(
    value = "/json/last/{moedas}",
    produces = { MediaType.APPLICATION_JSON_VALUE },
    consumes = { MediaType.APPLICATION_JSON_VALUE }
  )
  Response consultarCambioAtual(@PathVariable(value = "moedas") String moedas);

}
