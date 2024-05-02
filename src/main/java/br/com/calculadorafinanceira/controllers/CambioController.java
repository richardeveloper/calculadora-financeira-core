package br.com.calculadorafinanceira.controllers;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.responses.integration.CambioResponse;
import br.com.calculadorafinanceira.services.scheduled.EconomiaAwesomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/services/exchange")
public class CambioController {

  @Autowired
  private EconomiaAwesomeService economiaAwesomeService;

  @GetMapping(value = "/current")
  public ResponseEntity<CambioResponse> consultarCambioAtual() throws ServiceException {

    return ResponseEntity.ok(economiaAwesomeService.consultarCambioAtual());
  }

}
