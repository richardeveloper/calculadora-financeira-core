package br.com.calculadorafinanceira.repositories;

import br.com.calculadorafinanceira.entities.ParametroIrrf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ParametroIrrfRepository extends JpaRepository<ParametroIrrf, Long> {

  @Query(value = "SELECT irrf FROM ParametroIrrf irrf " +
                 "WHERE :salarioBruto >= irrf.valorMinimo " +
                 "AND :salarioBruto <= irrf.valorMaximo")
  Optional<ParametroIrrf> findBySalarioBruto(BigDecimal salarioBruto);
}
