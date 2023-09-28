package br.com.calculadorafinanceira.configs;

import br.com.calculadorafinanceira.entities.ParametroINSS;
import br.com.calculadorafinanceira.entities.ParametroIRRF;
import br.com.calculadorafinanceira.enums.FaixaSalarialINSS;
import br.com.calculadorafinanceira.enums.FaixaSalarialIRRF;
import br.com.calculadorafinanceira.repositories.ParametroINSSRepository;
import br.com.calculadorafinanceira.repositories.ParametroIRRFRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class SetupConfig implements CommandLineRunner {

  @Autowired
  private ParametroINSSRepository parametroINSSRepository;

  @Autowired
  private ParametroIRRFRepository parametroIRRFRepository;

  @Override
  public void run(String... args) throws Exception {

    ParametroINSS primeiraFaixaINSS = new ParametroINSS();
    primeiraFaixaINSS.setFaixaSalarial(FaixaSalarialINSS.PRIMEIRA_FAIXA_SALARIAL);
    primeiraFaixaINSS.setValorMinimo(BigDecimal.ZERO);
    primeiraFaixaINSS.setValorMaximo(new BigDecimal("1320.00"));
    primeiraFaixaINSS.setAliquota(7.5);

    ParametroINSS segundaFaixaINSS = new ParametroINSS();
    segundaFaixaINSS.setFaixaSalarial(FaixaSalarialINSS.SEGUNDA_FAIXA_SALARIAL);
    segundaFaixaINSS.setValorMinimo(new BigDecimal("1320.01"));
    segundaFaixaINSS.setValorMaximo(new BigDecimal("2571.29"));
    segundaFaixaINSS.setAliquota(9.0);

    ParametroINSS terceiraFaixaINSS = new ParametroINSS();
    terceiraFaixaINSS.setFaixaSalarial(FaixaSalarialINSS.TERCEIRA_FAIXA_SALARIAL);
    terceiraFaixaINSS.setValorMinimo(new BigDecimal("2571.30"));
    terceiraFaixaINSS.setValorMaximo(new BigDecimal("3856.94"));
    terceiraFaixaINSS.setAliquota(12.0);

    ParametroINSS quartaFaixaINSS = new ParametroINSS();
    quartaFaixaINSS.setFaixaSalarial(FaixaSalarialINSS.QUARTA_FAIXA_SALARIAL);
    quartaFaixaINSS.setValorMinimo(new BigDecimal("3856.95"));
    quartaFaixaINSS.setValorMaximo(new BigDecimal("7507.49"));
    quartaFaixaINSS.setAliquota(14.0);

    parametroINSSRepository.saveAll(List.of(primeiraFaixaINSS, segundaFaixaINSS, terceiraFaixaINSS, quartaFaixaINSS));

    ParametroIRRF faixaIsenta = new ParametroIRRF();
    faixaIsenta.setFaixaSalarial(FaixaSalarialIRRF.FAIXA_ISENTA);
    faixaIsenta.setValorMinimo(BigDecimal.ZERO);
    faixaIsenta.setValorMaximo(new BigDecimal("2112.00"));
    faixaIsenta.setParcelaDedutivel(BigDecimal.ZERO);
    faixaIsenta.setAliquota(0.0);

    ParametroIRRF primeiraFaixaIRRF = new ParametroIRRF();
    primeiraFaixaIRRF.setFaixaSalarial(FaixaSalarialIRRF.PRIMEIRA_FAIXA_SALARIAL);
    primeiraFaixaIRRF.setValorMinimo(new BigDecimal("2112.01"));
    primeiraFaixaIRRF.setValorMaximo(new BigDecimal("2826.65"));
    primeiraFaixaIRRF.setParcelaDedutivel(new BigDecimal("158.40"));
    primeiraFaixaIRRF.setAliquota(7.5);

    ParametroIRRF segundaFaixaIRRF = new ParametroIRRF();
    segundaFaixaIRRF.setFaixaSalarial(FaixaSalarialIRRF.SEGUNDA_FAIXA_SALARIAL);
    segundaFaixaIRRF.setValorMinimo(new BigDecimal("2826.66"));
    segundaFaixaIRRF.setValorMaximo(new BigDecimal("3751.05"));
    segundaFaixaIRRF.setParcelaDedutivel(new BigDecimal("370.40"));
    segundaFaixaIRRF.setAliquota(15.0);

    ParametroIRRF terceiraFaixaIRRF = new ParametroIRRF();
    terceiraFaixaIRRF.setFaixaSalarial(FaixaSalarialIRRF.TERCEIRA_FAIXA_SALARIAL);
    terceiraFaixaIRRF.setValorMinimo(new BigDecimal("3751.06"));
    terceiraFaixaIRRF.setValorMaximo(new BigDecimal("4664.68"));
    terceiraFaixaIRRF.setParcelaDedutivel(new BigDecimal("651.73"));
    terceiraFaixaIRRF.setAliquota(22.5);

    ParametroIRRF quartaFaixaIRRF = new ParametroIRRF();
    quartaFaixaIRRF.setFaixaSalarial(FaixaSalarialIRRF.QUARTA_FAIXA_SALARIAL);
    quartaFaixaIRRF.setValorMinimo(new BigDecimal("4664.69"));
    quartaFaixaIRRF.setValorMaximo(new BigDecimal(Integer.MAX_VALUE));
    quartaFaixaIRRF.setParcelaDedutivel(new BigDecimal("884.96"));
    quartaFaixaIRRF.setAliquota(27.5);

    parametroIRRFRepository.saveAll(List.of(faixaIsenta, primeiraFaixaIRRF, segundaFaixaIRRF, terceiraFaixaIRRF, quartaFaixaIRRF));
  }
}
