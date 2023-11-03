package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.requests.DecimoTerceiroRequest;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.requests.IrrfRequest;
import br.com.calculadorafinanceira.responses.DecimoTerceiroResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculadoraDecimoTerceiro {

  private static final Integer MONTHS_OF_YEAR = 12;
  private static final Integer SCALE_PRECISION = 10;

  @Autowired
  private CalculadoraIrrf calculadoraIRRF;

  @Autowired
  private CalculadoraInss calculadoraINSS;

  public DecimoTerceiroResponse calcularDecimoTerceiro(DecimoTerceiroRequest request) {

    try {

      BigDecimal decimoTerceiro = request.getSalarioBruto()
        .divide(BigDecimal.valueOf(MONTHS_OF_YEAR), SCALE_PRECISION, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(request.getMesesTrabalhados()))
        .setScale(2, RoundingMode.HALF_UP);

      BigDecimal primeiraParcela = decimoTerceiro.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

      InssRequest inssRequest = InssRequest.builder()
        .salarioBruto(decimoTerceiro.subtract(primeiraParcela))
        .build();

      BigDecimal inss = calculadoraINSS.calcularINSS(inssRequest).getInss();

      IrrfRequest irrfRequest = IrrfRequest.builder()
        .salarioBruto(decimoTerceiro.subtract(primeiraParcela))
        .dependentes(request.getDependentes())
        .build();

      BigDecimal irrf = calculadoraIRRF.calcularIRRF(irrfRequest).getIrrf();

      BigDecimal segundaParcela = decimoTerceiro.subtract(primeiraParcela)
        .subtract(inss)
        .subtract(irrf);

      BigDecimal parcelaUnica = primeiraParcela.add(segundaParcela);

      return DecimoTerceiroResponse.builder()
        .parcelaUnica(parcelaUnica)
        .primeiraParcela(primeiraParcela)
        .segundaParcela(segundaParcela)
        .build();

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }
}
