package huga721.github.spring.todo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@SpringBootApplication
public class SpringTodoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTodoAppApplication.class, args);
	}

	// javax Validator, used for processing annotations for example @NotBlank
	@Bean
	Validator validator() {
		return new LocalValidatorFactoryBean();
	}
}
