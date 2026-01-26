package com.niiran.software.solutions;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.niiran.software.solutions.repository")
@EntityScan(basePackages = "com.niiran.software.solutions.domain")
@OpenAPIDefinition(
        info = @Info(
                title = "RCM Engineering API",
                version = "v1",
                description = "REST API documentation"
        )
)
public class RcmEngineeringApplication {
	public static void main(String[] args) {
		SpringApplication.run(RcmEngineeringApplication.class, args);
        // swagger URL : http://localhost:8080/swagger-ui.html
	}

    @Autowired
    private Environment env;

    @PostConstruct
    public void checkProfile() {
        System.out.println("Active profiles: " + Arrays.toString(env.getActiveProfiles()));
    }
}
