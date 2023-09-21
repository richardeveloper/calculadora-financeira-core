package br.com.calculadorafinanceira.controllers;

import br.com.calculadorafinanceira.exceptions.ServiceException;
import br.com.calculadorafinanceira.services.CalculadoraFerias;
import br.com.calculadorafinanceira.services.CalculadoraINSS;
import br.com.calculadorafinanceira.services.CalculadoraIRRF;
import br.com.calculadorafinanceira.views.FeriasView;
import br.com.calculadorafinanceira.views.INSSView;
import br.com.calculadorafinanceira.views.IRRFView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/services")
public class CalculadoraFinanceiraController {

  @Autowired
  private CalculadoraINSS calculadoraINSS;

  @Autowired
  private CalculadoraIRRF calculadoraIRRF;

  @Autowired
  private CalculadoraFerias calculadoraFerias;

  @GetMapping(value = "/inss")
  public ResponseEntity<INSSView> calcularINSS(
    @RequestParam("salarioBruto") BigDecimal salarioBruto
  ) throws ServiceException {
    return ResponseEntity.ok(calculadoraINSS.calcularINSS(salarioBruto));
  }

  @GetMapping(value = "/irrf")
  public ResponseEntity<IRRFView> calcularIRRF(
    @RequestParam(value = "salarioBruto") BigDecimal salarioBruto,
    @RequestParam(value = "numeroDependentes", defaultValue = "0") Integer numeroDependentes
  ) throws ServiceException {
    return ResponseEntity.ok(calculadoraIRRF.calcularIRRF(salarioBruto, numeroDependentes));
  }

  @GetMapping(value = "/ferias")
  public ResponseEntity<FeriasView> calcularFerias(
    @RequestParam(value = "salarioBruto") BigDecimal salarioBruto,
    @RequestParam(value = "numeroDependentes", defaultValue = "0") Integer numeroDependentes,
    @RequestParam(value = "abonoPecuniario") Boolean abonoPecuniario,
    @RequestParam(value = "diasFerias", defaultValue = "30") Integer diasFerias
    ) throws ServiceException {
    return ResponseEntity.ok(calculadoraFerias.calcularFerias(salarioBruto, numeroDependentes,
      abonoPecuniario, diasFerias));
  }
}
