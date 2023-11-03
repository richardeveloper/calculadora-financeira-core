package br.com.calculadorafinanceira.repositories;

import br.com.calculadorafinanceira.entities.ParametroInss;
import br.com.calculadorafinanceira.enums.FaixaSalarialInss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametroInssRepository extends JpaRepository<ParametroInss, Long> {

  Optional<ParametroInss> findByFaixaSalarial(FaixaSalarialInss faixaSalarialEnum);

}
