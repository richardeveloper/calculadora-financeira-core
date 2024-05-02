package br.com.calculadorafinanceira.services.integration;

import br.com.calculadorafinanceira.requests.integration.TaxaCdiRequest;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "banco-central-api", url = "${banco-central-api.url}")
public interface BancoCentralIntegration {

  @PostMapping(
    value = "/novoselic/rest/taxaSelicApurada/pub/search",
    produces = { MediaType.APPLICATION_JSON_VALUE },
    consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE, MediaType.ALL_VALUE}
  )
  Response consultarTaxaCdi(
    @RequestBody TaxaCdiRequest request,
    @RequestParam(value = "parametrosOrdenacao") String parametrosOrdenacao,
    @RequestParam(value = "page") int page,
    @RequestParam(value = "pageSize") int pageSize
  );

}
