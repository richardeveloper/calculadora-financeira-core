package br.com.calculadorafinanceira.controllers;

import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.requests.DecimoTerceiroRequest;
import br.com.calculadorafinanceira.requests.FGTSRequest;
import br.com.calculadorafinanceira.requests.FeriasRequest;
import br.com.calculadorafinanceira.requests.INSSRequest;
import br.com.calculadorafinanceira.requests.IRRFRequest;
import br.com.calculadorafinanceira.requests.SalarioLiquidoRequest;
import br.com.calculadorafinanceira.responses.DecimoTerceiroResponse;
import br.com.calculadorafinanceira.responses.FGTSResponse;
import br.com.calculadorafinanceira.responses.FeriasResponse;
import br.com.calculadorafinanceira.responses.INSSResponse;
import br.com.calculadorafinanceira.responses.IRRFResponse;
import br.com.calculadorafinanceira.responses.SalarioLiquidoResponse;
import br.com.calculadorafinanceira.services.CalculadoraDecimoTerceiro;
import br.com.calculadorafinanceira.services.CalculadoraFGTS;
import br.com.calculadorafinanceira.services.CalculadoraFerias;
import br.com.calculadorafinanceira.services.CalculadoraINSS;
import br.com.calculadorafinanceira.services.CalculadoraIRRF;
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
  private CalculadoraINSS calculadoraINSS;

  @Autowired
  private CalculadoraIRRF calculadoraIRRF;

  @Autowired
  private CalculadoraFerias calculadoraFerias;

  @Autowired
  private CalculadoraDecimoTerceiro calculadoraDecimoTerceiro;

  @Autowired
  private CalculadoraSalarioLiquido calculadoraSalarioLiquido;

  @Autowired
  private CalculadoraFGTS calculadoraFGTS;

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
  public ResponseEntity<FGTSResponse> calcularFGTS(@Valid @RequestBody FGTSRequest request)
    throws ServiceException {

    return ResponseEntity.ok(calculadoraFGTS.calcularFGTS(request));
  }

}
