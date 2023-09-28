package br.com.calculadorafinanceira.repositories;

import br.com.calculadorafinanceira.entities.ParametroINSS;
import br.com.calculadorafinanceira.enums.FaixaSalarialINSS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametroINSSRepository extends JpaRepository<ParametroINSS, Long> {

  Optional<ParametroINSS> findByFaixaSalarial(FaixaSalarialINSS faixaSalarialEnum);

}
