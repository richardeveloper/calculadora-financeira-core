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
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile(value = "!prod")
public class SetupConfig implements CommandLineRunner {

  @Autowired
  private ParametroINSSRepository parametroINSSRepository;

  @Autowired
  private ParametroIRRFRepository parametroIRRFRepository;

  @Override
  public void run(String... args) throws Exception {

    ParametroINSS primeiraFaixaINSS = ParametroINSS.builder()
      .faixaSalarial(FaixaSalarialINSS.PRIMEIRA_FAIXA_SALARIAL)
      .valorMinimo(BigDecimal.ZERO)
      .valorMaximo(new BigDecimal("1320.00"))
      .aliquota(7.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroINSS segundaFaixaINSS = ParametroINSS.builder()
      .faixaSalarial(FaixaSalarialINSS.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("1320.01"))
      .valorMaximo(new BigDecimal("2571.29"))
      .aliquota(9.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroINSS terceiraFaixaINSS = ParametroINSS.builder()
      .faixaSalarial(FaixaSalarialINSS.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2571.30"))
      .valorMaximo(new BigDecimal("3856.94"))
      .aliquota(12.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroINSS quartaFaixaINSS = ParametroINSS.builder()
      .faixaSalarial(FaixaSalarialINSS.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("3856.95"))
      .valorMaximo(new BigDecimal("7507.49"))
      .aliquota(14.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    parametroINSSRepository.saveAll(List.of(primeiraFaixaINSS, segundaFaixaINSS, terceiraFaixaINSS, quartaFaixaINSS));

    ParametroIRRF faixaIsenta = ParametroIRRF.builder()
      .faixaSalarial(FaixaSalarialIRRF.FAIXA_ISENTA)
      .valorMinimo(BigDecimal.ZERO)
      .valorMaximo(new BigDecimal("2112.00"))
      .parcelaDedutivel(BigDecimal.ZERO)
      .aliquota(0.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroIRRF primeiraFaixaIRRF = ParametroIRRF.builder()
      .faixaSalarial(FaixaSalarialIRRF.PRIMEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2112.01"))
      .valorMaximo(new BigDecimal("2826.65"))
      .parcelaDedutivel(new BigDecimal("158.40"))
      .aliquota(7.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroIRRF segundaFaixaIRRF = ParametroIRRF.builder()
      .faixaSalarial(FaixaSalarialIRRF.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2826.66"))
      .valorMaximo(new BigDecimal("3751.05"))
      .parcelaDedutivel(new BigDecimal("370.40"))
      .aliquota(15.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroIRRF terceiraFaixaIRRF = ParametroIRRF.builder()
      .faixaSalarial(FaixaSalarialIRRF.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("3751.06"))
      .valorMaximo(new BigDecimal("4664.68"))
      .parcelaDedutivel(new BigDecimal("651.73"))
      .aliquota(22.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroIRRF quartaFaixaIRRF = ParametroIRRF.builder()
      .faixaSalarial(FaixaSalarialIRRF.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("4664.69"))
      .valorMaximo(new BigDecimal(Integer.MAX_VALUE))
      .parcelaDedutivel(new BigDecimal("884.96"))
      .aliquota(27.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    parametroIRRFRepository.saveAll(List.of(faixaIsenta, primeiraFaixaIRRF, segundaFaixaIRRF, terceiraFaixaIRRF, quartaFaixaIRRF));
  }
}
