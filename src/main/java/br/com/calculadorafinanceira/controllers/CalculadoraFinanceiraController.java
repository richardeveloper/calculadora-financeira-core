package br.com.calculadorafinanceira.controllers;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.FeriasRequest;
import br.com.calculadorafinanceira.requests.INSSRequest;
import br.com.calculadorafinanceira.requests.IRRFRequest;
import br.com.calculadorafinanceira.responses.FeriasResponse;
import br.com.calculadorafinanceira.responses.INSSResponse;
import br.com.calculadorafinanceira.responses.IRRFResponse;
import br.com.calculadorafinanceira.services.CalculadoraFerias;
import br.com.calculadorafinanceira.services.CalculadoraINSS;
import br.com.calculadorafinanceira.services.CalculadoraIRRF;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/services/calculadora")
public class CalculadoraFinanceiraController {

  @Autowired
  private CalculadoraINSS calculadoraINSS;

  @Autowired
  private CalculadoraIRRF calculadoraIRRF;

  @Autowired
  private CalculadoraFerias calculadoraFerias;

  @PostMapping(value = "/inss")
  public ResponseEntity<INSSResponse> calcularINSS(@Valid @RequestBody INSSRequest request) throws ServiceException {
    return ResponseEntity.ok(calculadoraINSS.calcularINSS(request));
  }

  @PostMapping(value = "/irrf")
  public ResponseEntity<IRRFResponse> calcularIRRF(@Valid @RequestBody IRRFRequest request) throws ServiceException {
    return ResponseEntity.ok(calculadoraIRRF.calcularIRRF(request));
  }

  @PostMapping(value = "/ferias")
  public ResponseEntity<FeriasResponse> calcularFerias(@Valid @RequestBody FeriasRequest request) throws ServiceException {
    return ResponseEntity.ok(calculadoraFerias.calcularFerias(request));
  }

}
