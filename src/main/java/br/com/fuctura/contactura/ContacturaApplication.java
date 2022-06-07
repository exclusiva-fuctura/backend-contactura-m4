package br.com.fuctura.contactura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication()
public class ContacturaApplication implements WebMvcConfigurer {

	// https://www.freecodecamp.org/portuguese/news/como-configurar-a-autenticacao-e-a-autorizacao-no-jwt-para-o-spring-boot-em-java/
	
	public static void main(String[] args) {
		SpringApplication.run(ContacturaApplication.class, args);
	}
	
	@Override
	public void addViewControllers (ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui.html");
    }
}
