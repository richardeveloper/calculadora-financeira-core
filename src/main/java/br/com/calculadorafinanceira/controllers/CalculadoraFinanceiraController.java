package br.com.calculadorafinanceira.controllers;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.JurosCompostosRequest;
import br.com.calculadorafinanceira.requests.JurosSimplesRequest;
import br.com.calculadorafinanceira.responses.JurosCompostosResponse;
import br.com.calculadorafinanceira.responses.JurosSimplesResponse;
import br.com.calculadorafinanceira.services.CalculadoraJuros;
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
  private CalculadoraJuros calculadoraJuros;

  @PostMapping(value = "/juros-simples")
  public ResponseEntity<JurosSimplesResponse> calcularJurosSimples(@Valid @RequestBody JurosSimplesRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraJuros.calcularJurosSimples(request));
  }

  @PostMapping(value = "/juros-compostos")
  public ResponseEntity<JurosCompostosResponse> calcularJurosCompostos(@Valid @RequestBody JurosCompostosRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraJuros.calcularJurosCompostos(request));
  }

}
