package br.com.calculadorafinanceira.entities;

import br.com.calculadorafinanceira.enums.FaixaSalarialINSS;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PARAMETRO_INSS")
public class ParametroINSS {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "FAIXA_SALARIAL")
  @Enumerated(value = EnumType.STRING)
  private FaixaSalarialINSS faixaSalarial;

  @Column(name = "VALOR_MINIMO")
  private BigDecimal valorMinimo;

  @Column(name = "VALOR_MAXIMO")
  private BigDecimal valorMaximo;

  @Column(name = "ALIQUOTA")
  private Double aliquota;

  @Column(name = "DATA_CADASTRO")
  private LocalDateTime dataCadastro;

  @Column(name = "ULTIMA_ATUALIZACAO")
  private LocalDateTime ultimaAtualizacao;

}
