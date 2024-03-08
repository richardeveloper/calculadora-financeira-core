package br.com.calculadorafinanceira.controllers;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.RendimentoCdiRequest;
import br.com.calculadorafinanceira.responses.RendimentoCdiResponse;
import br.com.calculadorafinanceira.services.CalculadoraRendimento;

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
  private CalculadoraRendimento calculadoraRendimento;

  @PostMapping(value = "/rendimento-cdi")
  public ResponseEntity<RendimentoCdiResponse> calcularRendimentoCdi(@Valid @RequestBody RendimentoCdiRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraRendimento.calcularRendimentoCdi(request));
  }
}
