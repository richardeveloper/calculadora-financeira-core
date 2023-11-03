package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroInss;
import br.com.calculadorafinanceira.enums.FaixaSalarialInss;
import br.com.calculadorafinanceira.exceptions.models.ServiceException;
import br.com.calculadorafinanceira.repositories.ParametroInssRepository;
import br.com.calculadorafinanceira.requests.InssRequest;
import br.com.calculadorafinanceira.responses.InssResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculadoraInss {

  @Autowired
  private ParametroInssRepository parametroINSSRepository;

  public InssResponse calcularINSS(InssRequest request) {

    try {

      BigDecimal salarioBruto = request.getSalarioBruto();

      if (salarioBruto.compareTo(BigDecimal.ZERO) == 0) {
        return new InssResponse(BigDecimal.ZERO, 0.0);
      }

      BigDecimal inss = BigDecimal.ZERO;

      ParametroInss primeiraFaixa = parametroINSSRepository
        .findByFaixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL)
        .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da primeira faixa salarial."));

      if (isFaixaSalarialCorrespondente(salarioBruto, primeiraFaixa.getValorMaximo())) {
        BigDecimal baseParaCalculo = calcularValorFaixaSalarial(salarioBruto, primeiraFaixa.getAliquota());

        return InssResponse.builder()
          .inss(inss.add(baseParaCalculo))
          .aliquota(primeiraFaixa.getAliquota())
          .build();
      }

      inss = inss.add(
        calcularValorFaixaSalarial(primeiraFaixa.getValorMaximo(), primeiraFaixa.getAliquota())
      );

      ParametroInss segundaFaixa = parametroINSSRepository
        .findByFaixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL)
        .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da segunda faixa salarial."));

      if (isFaixaSalarialCorrespondente(salarioBruto, segundaFaixa.getValorMaximo())) {
        BigDecimal baseParaCalculo = salarioBruto.subtract(primeiraFaixa.getValorMaximo());

        return InssResponse.builder()
          .inss(inss.add(calcularValorFaixaSalarial(baseParaCalculo, segundaFaixa.getAliquota())))
          .aliquota(segundaFaixa.getAliquota())
          .build();
      }

      inss = inss.add(
        calcularValorFaixaSalarial(segundaFaixa.getValorMaximo().subtract(primeiraFaixa.getValorMaximo()), segundaFaixa.getAliquota())
      );

      ParametroInss terceiraFaixa = parametroINSSRepository
        .findByFaixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL)
        .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da terceira faixa salarial."));

      if (isFaixaSalarialCorrespondente(salarioBruto, terceiraFaixa.getValorMaximo())) {
        BigDecimal baseParaCalculo = salarioBruto.subtract(segundaFaixa.getValorMaximo());

        return InssResponse.builder()
          .inss(inss.add(calcularValorFaixaSalarial(baseParaCalculo, terceiraFaixa.getAliquota())))
          .aliquota(terceiraFaixa.getAliquota())
          .build();
      }

      inss = inss.add(
        calcularValorFaixaSalarial(terceiraFaixa.getValorMaximo().subtract(segundaFaixa.getValorMaximo()), terceiraFaixa.getAliquota())
      );

      ParametroInss quartaFaixa = parametroINSSRepository
        .findByFaixaSalarial(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL)
        .orElseThrow(() -> new ServiceException("Não foi possível recuperar informações da quarta faixa salarial."));

      BigDecimal baseParaCalculo;

      if (isFaixaSalarialCorrespondente(salarioBruto, quartaFaixa.getValorMaximo())) {
        baseParaCalculo = salarioBruto.subtract(quartaFaixa.getValorMinimo());
      } else {
        baseParaCalculo = quartaFaixa.getValorMaximo().subtract(quartaFaixa.getValorMinimo());
      }

      inss = inss.add(calcularValorFaixaSalarial(baseParaCalculo, quartaFaixa.getAliquota()));

      return InssResponse.builder()
        .inss(inss.setScale(2, RoundingMode.HALF_UP))
        .aliquota(quartaFaixa.getAliquota())
        .build();

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
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
