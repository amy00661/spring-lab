package com.example.springlab.config;

import com.example.springlab.component.JwtTokenProvider;
import com.example.springlab.repository.UserRepository;
import com.example.springlab.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Instant;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    // 暴露 AuthenticationManager，供 Controller 呼叫 authenticate(...)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 提供密碼編碼器（請確保資料庫中的密碼是用同樣算法編碼）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 提供 UserDetailsService，讓 Spring 自動使用預設的 DaoAuthenticationProvider
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found: " + username));
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
                // 新增：未認證時統一回傳 JSON（此處處理在過濾器階段被攔下的請求）
                .exceptionHandling(e -> e.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    String body = """
                {
                  "timestamp": "%s",
                  "status": 401,
                  "error": "Unauthorized",
                  "message": "Full authentication is required to access this resource",
                  "path": "%s"
                }
                """.formatted(Instant.now().toString(), request.getRequestURI());
                    response.getWriter().write(body);
                }))
                .authorizeHttpRequests(auth -> auth
                // Swagger 放行
                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()
                // 登入 API 放行
                .requestMatchers("/auth/login").permitAll()
                // 其他 API 都要驗證
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
