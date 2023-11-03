package br.com.calculadorafinanceira.controllers;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.*;
import br.com.calculadorafinanceira.responses.*;
import br.com.calculadorafinanceira.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/services/calculadora")
public class CalculadoraTrabalhistaController {

  @Autowired
  private CalculadoraInss calculadoraINSS;

  @Autowired
  private CalculadoraIrrf calculadoraIRRF;

  @Autowired
  private CalculadoraFerias calculadoraFerias;

  @Autowired
  private CalculadoraDecimoTerceiro calculadoraDecimoTerceiro;

  @Autowired
  private CalculadoraSalarioLiquido calculadoraSalarioLiquido;

  @Autowired
  private CalculadoraFgts calculadoraFGTS;

  @PostMapping(value = "/inss")
  public ResponseEntity<InssResponse> calcularINSS(@Valid @RequestBody InssRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraINSS.calcularINSS(request));
  }

  @PostMapping(value = "/irrf")
  public ResponseEntity<IrrfResponse> calcularIRRF(@Valid @RequestBody IrrfRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraIRRF.calcularIRRF(request));
  }

  @PostMapping(value = "/ferias")
  public ResponseEntity<FeriasResponse> calcularFerias(@Valid @RequestBody FeriasRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraFerias.calcularFerias(request));
  }

  @PostMapping(value = "/decimo-terceiro")
  public ResponseEntity<DecimoTerceiroResponse> calcularDecimoTerceiro(@Valid @RequestBody DecimoTerceiroRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraDecimoTerceiro.calcularDecimoTerceiro(request));
  }

  @PostMapping(value = "/salario-liquido")
  public ResponseEntity<SalarioLiquidoResponse> calcularSalarioLiquido(@Valid @RequestBody SalarioLiquidoRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraSalarioLiquido.calcularSalarioLiquido(request));
  }

  @PostMapping(value = "/fgts")
  public ResponseEntity<FgtsResponse> calcularFGTS(@Valid @RequestBody FgtsRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraFGTS.calcularFGTS(request));
  }

}
