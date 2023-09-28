package br.com.calculadorafinanceira.repositories;

import br.com.calculadorafinanceira.entities.ParametroIRRF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ParametroIRRFRepository extends JpaRepository<ParametroIRRF, Long> {

  @Query(value = "SELECT irrf FROM ParametroIRRF irrf " +
                 "WHERE :salarioBruto >= irrf.valorMinimo " +
                 "AND :salarioBruto <= irrf.valorMaximo")
  Optional<ParametroIRRF> findBySalarioBruto(BigDecimal salarioBruto);
}
