package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroINSS;
import br.com.calculadorafinanceira.enums.FaixaSalarialINSS;
import br.com.calculadorafinanceira.exceptions.ServiceException;
import br.com.calculadorafinanceira.exceptions.ValidationException;
import br.com.calculadorafinanceira.repositories.ParametroINSSRepository;
import br.com.calculadorafinanceira.views.INSSView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculadoraINSS {

  private static final Logger LOGGER = LoggerFactory.getLogger(CalculadoraINSS.class);

  @Autowired
  private ParametroINSSRepository parametroINSSRepository;

  public INSSView calcularINSS(BigDecimal salarioBruto) {
    validarParametros(salarioBruto);

    BigDecimal INSS = BigDecimal.ZERO;

    ParametroINSS primeiraFaixa = parametroINSSRepository
      .findByFaixaSalarial(FaixaSalarialINSS.PRIMEIRA_FAIXA_SALARIAL)
      .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da primeira faixa salarial."));

    if (isFaixaSalarialCorrespondente(salarioBruto, primeiraFaixa.getValorMaximo())) {
      return INSSView.builder()
        .inss(INSS.add(calcularValorFaixaSalarial(salarioBruto, primeiraFaixa.getAliquota())))
        .aliquota(primeiraFaixa.getAliquota())
        .build();
    } else {
      INSS = INSS.add(calcularValorFaixaSalarial(primeiraFaixa.getValorMaximo(), primeiraFaixa.getAliquota()));
    }

    ParametroINSS segundaFaixa = parametroINSSRepository
      .findByFaixaSalarial(FaixaSalarialINSS.SEGUNDA_FAIXA_SALARIAL)
      .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da segunda faixa salarial."));

    if (isFaixaSalarialCorrespondente(salarioBruto, segundaFaixa.getValorMaximo())) {
      BigDecimal baseParaCalculo = salarioBruto.subtract(primeiraFaixa.getValorMaximo());

      return INSSView.builder()
        .inss(INSS.add(calcularValorFaixaSalarial(baseParaCalculo, segundaFaixa.getAliquota())))
        .aliquota(segundaFaixa.getAliquota())
        .build();
    }

    INSS = INSS.add(
      calcularValorFaixaSalarial(segundaFaixa.getValorMaximo().subtract(primeiraFaixa.getValorMaximo()), segundaFaixa.getAliquota())
    );

    ParametroINSS terceiraFaixa = parametroINSSRepository
      .findByFaixaSalarial(FaixaSalarialINSS.TERCEIRA_FAIXA_SALARIAL)
      .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da terceira faixa salarial."));

    if (isFaixaSalarialCorrespondente(salarioBruto, terceiraFaixa.getValorMaximo())) {
      BigDecimal baseParaCalculo = salarioBruto.subtract(segundaFaixa.getValorMaximo());

      return INSSView.builder()
        .inss(INSS.add(calcularValorFaixaSalarial(baseParaCalculo, terceiraFaixa.getAliquota())))
        .aliquota(terceiraFaixa.getAliquota())
        .build();
    }

    INSS = INSS.add(
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

    INSS = INSS.add(calcularValorFaixaSalarial(baseParaCalculo, quartaFaixa.getAliquota()));

    return INSSView.builder()
      .inss(INSS.setScale(2, RoundingMode.HALF_UP))
      .aliquota(quartaFaixa.getAliquota())
      .build();
  }

  private void validarParametros(BigDecimal salarioBruto) throws ValidationException {
    if (salarioBruto == null) {
      throw new ValidationException("O campo salarioBruto é obrigatório.");
    }

    if (salarioBruto.compareTo(BigDecimal.ZERO) < 0) {
      throw new ValidationException("O campo salarioBruto deve ser positivo.");
    }
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
