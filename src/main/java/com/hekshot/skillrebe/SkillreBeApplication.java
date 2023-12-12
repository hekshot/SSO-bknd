package com.hekshot.skillrebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SkillreBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillreBeApplication.class, args);
	}
}
