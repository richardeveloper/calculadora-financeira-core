package br.com.calculadorafinanceira.entities;

import br.com.calculadorafinanceira.enums.FaixaSalarialINSS;
import br.com.calculadorafinanceira.enums.FaixaSalarialIRRF;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "PARAMETRO_IRRF")
public class ParametroIRRF {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "FAIXA_SALARIAL")
  @Enumerated(value = EnumType.STRING)
  private FaixaSalarialIRRF faixaSalarial;

  @Column(name = "VALOR_MINIMO")
  private BigDecimal valorMinimo;

  @Column(name = "VALOR_MAXIMO")
  private BigDecimal valorMaximo;

  @Column(name = "PARCELA_DEDUTIVEL")
  private BigDecimal parcelaDedutivel;

  @Column(name = "ALIQUOTA")
  private Double aliquota;

}
