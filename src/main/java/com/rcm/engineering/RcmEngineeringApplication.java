package com.rcm.engineering;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class RcmEngineeringApplication {
	public static void main(String[] args) {
		SpringApplication.run(RcmEngineeringApplication.class, args);
	}

    @Autowired
    private Environment env;

    @PostConstruct
    public void checkProfile() {
        System.out.println("Active profiles: " + Arrays.toString(env.getActiveProfiles()));
    }
}
