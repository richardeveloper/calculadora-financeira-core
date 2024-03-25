package br.com.calculadorafinanceira.services;

import br.com.calculadorafinanceira.entities.ParametroInssEntity;
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
import java.util.List;

@Slf4j
@Service
public class CalculadoraInss {

  private static final double PERCENTAGE_DIVISOR = 100.0;

  @Autowired
  private ParametroInssRepository parametroInssRepository;

  public InssResponse calcularInss(InssRequest request) throws ServiceException {

    try {

      BigDecimal salarioBruto = request.getSalarioBruto();

      if (salarioBruto.compareTo(BigDecimal.ZERO) == 0) {
        return new InssResponse();
      }

      BigDecimal inss = BigDecimal.ZERO;

      List<ParametroInssEntity> parametroInssList = parametroInssRepository.findAll();

      if (parametroInssList.isEmpty()) {
        throw new ServiceException("Não foi possível recuperar os dados para cálculo do INSS.");
      }

      ParametroInssEntity primeiraFaixa = parametroInssList
        .stream()
        .filter(e -> e.getFaixaSalarial().equals(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL))
        .findFirst()
        .orElseThrow(() -> new ServiceException("Não foi possível recuperar os dados da primeira faixa salarial."));

      if (isFaixaSalarialCorrespondente(salarioBruto, primeiraFaixa.getValorMaximo())) {
        BigDecimal baseParaCalculo = calcularValorFaixaSalarial(salarioBruto, primeiraFaixa.getAliquota());

        return InssResponse.builder()
          .inss(inss.add(baseParaCalculo))
          .aliquota(primeiraFaixa.getAliquota())
          .build();
      }

      BigDecimal valorPrimeiraFaixa = calcularValorFaixaSalarial(primeiraFaixa.getValorMaximo(),
        primeiraFaixa.getAliquota());

      inss = inss.add(valorPrimeiraFaixa);

      ParametroInssEntity segundaFaixa = parametroInssList
        .stream()
        .filter(e -> e.getFaixaSalarial().equals(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL))
        .findFirst()
        .orElseThrow(() -> new ServiceException("Não foi possível recuperar os dados da segunda faixa salarial."));

      if (isFaixaSalarialCorrespondente(salarioBruto, segundaFaixa.getValorMaximo())) {
        BigDecimal baseParaCalculo = salarioBruto.subtract(primeiraFaixa.getValorMaximo());

        return InssResponse.builder()
          .inss(inss.add(calcularValorFaixaSalarial(baseParaCalculo, segundaFaixa.getAliquota())))
          .aliquota(segundaFaixa.getAliquota())
          .build();
      }

      BigDecimal valorSegundaFaixa = calcularValorFaixaSalarial(
        segundaFaixa.getValorMaximo().subtract(segundaFaixa.getValorMinimo()),
        segundaFaixa.getAliquota());

      inss = inss.add(valorSegundaFaixa);

      ParametroInssEntity terceiraFaixa = parametroInssList
        .stream()
        .filter(e -> e.getFaixaSalarial().equals(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL))
        .findFirst()
        .orElseThrow(() -> new ServiceException("Não foi possível recuperar os dados da terceira faixa salarial."));

      if (isFaixaSalarialCorrespondente(salarioBruto, terceiraFaixa.getValorMaximo())) {
        BigDecimal baseParaCalculo = salarioBruto.subtract(segundaFaixa.getValorMaximo());

        return InssResponse.builder()
          .inss(inss.add(calcularValorFaixaSalarial(baseParaCalculo, terceiraFaixa.getAliquota())))
          .aliquota(terceiraFaixa.getAliquota())
          .build();
      }

      BigDecimal valorTerceiraFaixa = calcularValorFaixaSalarial(
        terceiraFaixa.getValorMaximo().subtract(terceiraFaixa.getValorMinimo()),
        terceiraFaixa.getAliquota());

      inss = inss.add(valorTerceiraFaixa);

      ParametroInssEntity quartaFaixa = parametroInssList
        .stream()
        .filter(e -> e.getFaixaSalarial().equals(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL))
        .findFirst()
        .orElseThrow(() -> new ServiceException("Não foi possível recuperar os dados da quarta faixa salarial."));

      BigDecimal baseParaCalculo;

      if (isFaixaSalarialCorrespondente(salarioBruto, quartaFaixa.getValorMaximo())) {
        baseParaCalculo = salarioBruto.subtract(quartaFaixa.getValorMinimo());
      } else {
        baseParaCalculo = quartaFaixa.getValorMaximo().subtract(quartaFaixa.getValorMinimo());
      }

      BigDecimal valorQuartaFaixa = calcularValorFaixaSalarial(baseParaCalculo, quartaFaixa.getAliquota());

      inss = inss.add(valorQuartaFaixa);

      return InssResponse.builder()
        .inss(inss.setScale(2, RoundingMode.FLOOR))
        .aliquota(quartaFaixa.getAliquota())
        .build();

    }
    catch (ServiceException e) {
      throw e;
    }
    catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Desculpe, não foi possível completar a solicitação.");
    }
  }

  private BigDecimal calcularValorFaixaSalarial(BigDecimal salarioBruto, Double aliquota) {
    return salarioBruto
      .multiply(BigDecimal.valueOf(aliquota / PERCENTAGE_DIVISOR))
      .setScale(2, RoundingMode.HALF_UP);
  }

  private boolean isFaixaSalarialCorrespondente(BigDecimal salarioBruto, BigDecimal valorMaximoFaixaSalarial) {
    return salarioBruto.compareTo(valorMaximoFaixaSalarial) <= 0;
  }

}
