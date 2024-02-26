package br.com.calculadorafinanceira;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(value = "br.com.calculadorafinanceira.entities")
@ComponentScans({
	@ComponentScan(value = "br.com.calculadorafinanceira.configs"),
	@ComponentScan(value = "br.com.calculadorafinanceira.services"),
	@ComponentScan(value = "br.com.calculadorafinanceira.requests"),
	@ComponentScan(value = "br.com.calculadorafinanceira.responses"),
	@ComponentScan(value = "br.com.calculadorafinanceira.exceptions"),
	@ComponentScan(value = "br.com.calculadorafinanceira.repositories")
})
@EnableJpaRepositories
@SpringBootApplication
public class CalculadoraFinanceiraApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculadoraFinanceiraApplication.class, args);
	}

}