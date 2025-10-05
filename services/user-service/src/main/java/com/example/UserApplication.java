package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

    // 測試密碼是否匹配
    @Bean
    public CommandLineRunner testPassword(PasswordEncoder passwordEncoder) {
        return args -> {
            String rawPassword = "password";
            String encodedPassword = "$2a$10$Dow1h8tFfXlWjEyhjP7T4eVxqB4Vw/0YQcbNp3N3bfAq5xWgY8ZoK";

            String raw = "password";
            String encoded = passwordEncoder.encode(raw);
            System.out.println("Encoded: " + encoded);
            boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
            System.out.println("Password matches: " + matches); // 應該輸出 true
        };
    }
}
