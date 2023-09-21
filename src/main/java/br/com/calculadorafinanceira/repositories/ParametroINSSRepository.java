package br.com.calculadorafinanceira.repositories;

import br.com.calculadorafinanceira.entities.ParametroINSS;
import br.com.calculadorafinanceira.enums.FaixaSalarialINSS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ParametroINSSRepository extends JpaRepository<ParametroINSS, Long> {


  @Query(value = "SELECT inss.faixaSalarial FROM ParametroINSS inss " +
                 "WHERE " +
                 "(:salarioBruto >= inss.valorMinimo AND :salarioBruto <= inss.valorMaximo) " +
                 "OR " +
                 "(:salarioBruto >= inss.valorMinimo AND inss.valorMaximo < :salarioBruto)")
  Optional<FaixaSalarialINSS> findBySalarioBruto(BigDecimal salarioBruto);

  Optional<ParametroINSS> findByFaixaSalarial(FaixaSalarialINSS faixaSalarialEnum);
}
