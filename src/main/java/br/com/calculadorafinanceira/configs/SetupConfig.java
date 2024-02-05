package br.com.calculadorafinanceira.configs;

import br.com.calculadorafinanceira.entities.ParametroInss;
import br.com.calculadorafinanceira.entities.ParametroIrrf;
import br.com.calculadorafinanceira.enums.FaixaSalarialInss;
import br.com.calculadorafinanceira.enums.FaixaSalarialIrrf;
import br.com.calculadorafinanceira.repositories.ParametroInssRepository;
import br.com.calculadorafinanceira.repositories.ParametroIrrfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile(value = "local")
public class SetupConfig implements CommandLineRunner {

  @Autowired
  private ParametroInssRepository parametroInssRepository;

  @Autowired
  private ParametroIrrfRepository parametroIrrfRepository;

  @Override
  public void run(String... args) throws Exception {

    ParametroInss primeiraFaixaInss = ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.PRIMEIRA_FAIXA_SALARIAL)
      .valorMinimo(BigDecimal.ZERO)
      .valorMaximo(new BigDecimal("1412.00"))
      .aliquota(7.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroInss segundaFaixaInss = ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("1412.01"))
      .valorMaximo(new BigDecimal("2666.68"))
      .aliquota(9.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroInss terceiraFaixaInss = ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2666.69"))
      .valorMaximo(new BigDecimal("4000.03"))
      .aliquota(12.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroInss quartaFaixaInss = ParametroInss.builder()
      .faixaSalarial(FaixaSalarialInss.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("4000.04"))
      .valorMaximo(new BigDecimal("7786.02"))
      .aliquota(14.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    parametroInssRepository.saveAll(List.of(primeiraFaixaInss, segundaFaixaInss, terceiraFaixaInss, quartaFaixaInss));

    ParametroIrrf faixaIsenta = ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.FAIXA_ISENTA)
      .valorMinimo(BigDecimal.ZERO)
      .valorMaximo(new BigDecimal("2112.00"))
      .parcelaDedutivel(BigDecimal.ZERO)
      .aliquota(0.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroIrrf primeiraFaixaIrrf = ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.PRIMEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2112.01"))
      .valorMaximo(new BigDecimal("2826.65"))
      .parcelaDedutivel(new BigDecimal("158.40"))
      .aliquota(7.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroIrrf segundaFaixaIrrf = ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.SEGUNDA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("2826.66"))
      .valorMaximo(new BigDecimal("3751.05"))
      .parcelaDedutivel(new BigDecimal("370.40"))
      .aliquota(15.0)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroIrrf terceiraFaixaIrrf = ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.TERCEIRA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("3751.06"))
      .valorMaximo(new BigDecimal("4664.68"))
      .parcelaDedutivel(new BigDecimal("651.73"))
      .aliquota(22.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    ParametroIrrf quartaFaixaIrrf = ParametroIrrf.builder()
      .faixaSalarial(FaixaSalarialIrrf.QUARTA_FAIXA_SALARIAL)
      .valorMinimo(new BigDecimal("4664.69"))
      .valorMaximo(new BigDecimal("999999999999.99"))
      .parcelaDedutivel(new BigDecimal("884.96"))
      .aliquota(27.5)
      .dataCadastro(LocalDateTime.now())
      .ultimaAtualizacao(LocalDateTime.now())
      .build();

    parametroIrrfRepository.saveAll(List.of(faixaIsenta, primeiraFaixaIrrf, segundaFaixaIrrf, terceiraFaixaIrrf, quartaFaixaIrrf));
  }
}
