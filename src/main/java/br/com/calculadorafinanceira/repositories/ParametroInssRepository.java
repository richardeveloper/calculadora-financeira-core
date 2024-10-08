package br.com.calculadorafinanceira.repositories;

import br.com.calculadorafinanceira.entities.ParametroInssEntity;
import br.com.calculadorafinanceira.enums.FaixaSalarialInss;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametroInssRepository extends JpaRepository<ParametroInssEntity, Long> {

  Optional<ParametroInssEntity> findByFaixaSalarial(FaixaSalarialInss faixaSalarialEnum);

}
