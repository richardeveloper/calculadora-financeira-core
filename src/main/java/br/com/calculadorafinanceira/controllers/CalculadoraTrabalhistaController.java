package br.com.calculadorafinanceira.controllers;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.DecimoTerceiroRequest;
import br.com.calculadorafinanceira.requests.FeriasRequest;
import br.com.calculadorafinanceira.requests.FgtsRequest;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.requests.SalarioLiquidoRequest;
import br.com.calculadorafinanceira.responses.DecimoTerceiroResponse;
import br.com.calculadorafinanceira.responses.FeriasResponse;
import br.com.calculadorafinanceira.responses.FgtsResponse;
import br.com.calculadorafinanceira.responses.InssResponse;
import br.com.calculadorafinanceira.responses.IrrfResponse;
import br.com.calculadorafinanceira.responses.SalarioLiquidoResponse;
import br.com.calculadorafinanceira.services.CalculadoraDecimoTerceiro;
import br.com.calculadorafinanceira.services.CalculadoraFerias;
import br.com.calculadorafinanceira.services.CalculadoraFgts;
import br.com.calculadorafinanceira.services.CalculadoraInss;
import br.com.calculadorafinanceira.services.CalculadoraIrrf;
import br.com.calculadorafinanceira.services.CalculadoraSalarioLiquido;

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
  private CalculadoraInss calculadoraInss;

  @Autowired
  private CalculadoraIrrf calculadoraIrrf;

  @Autowired
  private CalculadoraFerias calculadoraFerias;

  @Autowired
  private CalculadoraDecimoTerceiro calculadoraDecimoTerceiro;

  @Autowired
  private CalculadoraSalarioLiquido calculadoraSalarioLiquido;

  @Autowired
  private CalculadoraFgts calculadoraFgts;

  @PostMapping(value = "/inss")
  public ResponseEntity<InssResponse> calcularInss(@Valid @RequestBody InssRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraInss.calcularInss(request));
  }

  @PostMapping(value = "/irrf")
  public ResponseEntity<IrrfResponse> calcularIrrf(@Valid @RequestBody IrrfRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraIrrf.calcularIrrf(request));
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
  public ResponseEntity<FgtsResponse> calcularFgts(@Valid @RequestBody FgtsRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraFgts.calcularFgts(request));
  }

}
