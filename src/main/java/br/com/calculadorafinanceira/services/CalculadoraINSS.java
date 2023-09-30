package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroINSS;
import br.com.calculadorafinanceira.enums.FaixaSalarialINSS;
import br.com.calculadorafinanceira.exceptions.ServiceException;
import br.com.calculadorafinanceira.repositories.ParametroINSSRepository;
import br.com.calculadorafinanceira.requests.INSSRequest;
import br.com.calculadorafinanceira.responses.INSSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculadoraINSS {

  @Autowired
  private ParametroINSSRepository parametroINSSRepository;

  public INSSResponse calcularINSS(INSSRequest request) {

    BigDecimal salarioBruto = request.getSalarioBruto();

    if (salarioBruto.compareTo(BigDecimal.ZERO) == 0) {
      return new INSSResponse(BigDecimal.ZERO, 0.0);
    }

    BigDecimal inss = BigDecimal.ZERO;

    ParametroINSS primeiraFaixa = parametroINSSRepository
      .findByFaixaSalarial(FaixaSalarialINSS.PRIMEIRA_FAIXA_SALARIAL)
      .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da primeira faixa salarial."));

    if (isFaixaSalarialCorrespondente(salarioBruto, primeiraFaixa.getValorMaximo())) {
      BigDecimal baseParaCalculo = calcularValorFaixaSalarial(salarioBruto, primeiraFaixa.getAliquota());

      return INSSResponse.builder()
        .inss(inss.add(baseParaCalculo))
        .aliquota(primeiraFaixa.getAliquota())
        .build();
    }

    inss = inss.add(
      calcularValorFaixaSalarial(primeiraFaixa.getValorMaximo(), primeiraFaixa.getAliquota())
    );

    ParametroINSS segundaFaixa = parametroINSSRepository
      .findByFaixaSalarial(FaixaSalarialINSS.SEGUNDA_FAIXA_SALARIAL)
      .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da segunda faixa salarial."));

    if (isFaixaSalarialCorrespondente(salarioBruto, segundaFaixa.getValorMaximo())) {
      BigDecimal baseParaCalculo = salarioBruto.subtract(primeiraFaixa.getValorMaximo());

      return INSSResponse.builder()
        .inss(inss.add(calcularValorFaixaSalarial(baseParaCalculo, segundaFaixa.getAliquota())))
        .aliquota(segundaFaixa.getAliquota())
        .build();
    }

    inss = inss.add(
      calcularValorFaixaSalarial(segundaFaixa.getValorMaximo().subtract(primeiraFaixa.getValorMaximo()), segundaFaixa.getAliquota())
    );

    ParametroINSS terceiraFaixa = parametroINSSRepository
      .findByFaixaSalarial(FaixaSalarialINSS.TERCEIRA_FAIXA_SALARIAL)
      .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da terceira faixa salarial."));

    if (isFaixaSalarialCorrespondente(salarioBruto, terceiraFaixa.getValorMaximo())) {
      BigDecimal baseParaCalculo = salarioBruto.subtract(segundaFaixa.getValorMaximo());

      return INSSResponse.builder()
        .inss(inss.add(calcularValorFaixaSalarial(baseParaCalculo, terceiraFaixa.getAliquota())))
        .aliquota(terceiraFaixa.getAliquota())
        .build();
    }

    inss = inss.add(
      calcularValorFaixaSalarial(terceiraFaixa.getValorMaximo().subtract(segundaFaixa.getValorMaximo()), terceiraFaixa.getAliquota())
    );

    ParametroINSS quartaFaixa = parametroINSSRepository
      .findByFaixaSalarial(FaixaSalarialINSS.QUARTA_FAIXA_SALARIAL)
      .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da quarta faixa salarial."));

    BigDecimal baseParaCalculo;

    if (isFaixaSalarialCorrespondente(salarioBruto, quartaFaixa.getValorMaximo())) {
      baseParaCalculo = salarioBruto.subtract(quartaFaixa.getValorMinimo());
    } else {
      baseParaCalculo = quartaFaixa.getValorMaximo().subtract(quartaFaixa.getValorMinimo());
    }

    inss = inss.add(calcularValorFaixaSalarial(baseParaCalculo, quartaFaixa.getAliquota()));

    return INSSResponse.builder()
      .inss(inss.setScale(2, RoundingMode.HALF_UP))
      .aliquota(quartaFaixa.getAliquota())
      .build();
  }

  private BigDecimal calcularValorFaixaSalarial(BigDecimal salarioBruto, Double aliquota) {
    return salarioBruto
      .multiply(BigDecimal.valueOf(aliquota / 100.00))
      .setScale(2, RoundingMode.HALF_UP);
  }

  private boolean isFaixaSalarialCorrespondente(BigDecimal salarioBruto, BigDecimal valorMaximoFaixaSalarial) {
    return salarioBruto.compareTo(valorMaximoFaixaSalarial) <= 0;
  }
}
