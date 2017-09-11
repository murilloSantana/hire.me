package br.com.encurtadorurl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** Classe Inicializadora no projeto
 * @author Murillo Santana
 * @version 1.0.0
 */

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages="br.com.encurtadorurl")
@EnableJpaRepositories(basePackages="br.com.encurtadorurl.dao")
@EntityScan(basePackages="br.com.encurtadorurl.model")
public class Application extends SpringBootServletInitializer{

	public static void main(String[] args) throws IOException, URISyntaxException {
		SpringApplication.run(Application.class);

	}

}
