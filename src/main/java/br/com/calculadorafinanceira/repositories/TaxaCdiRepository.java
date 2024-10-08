package br.com.calculadorafinanceira.repositories;

import br.com.calculadorafinanceira.entities.TaxaCdiEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxaCdiRepository extends JpaRepository<TaxaCdiEntity, Long> {

  @Query(value = "SELECT taxaCdi FROM TaxaCdiEntity taxaCdi ORDER BY taxaCdi.dataConsulta DESC LIMIT 1")
  Optional<TaxaCdiEntity> findLastRegistered();

}
