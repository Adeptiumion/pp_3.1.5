package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(
                        AbstractHttpConfigurer::disable

                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/admin/**").hasRole("ADMIN") // На "админку" попадет только админ.
                                .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/login").permitAll() // Залогиниться может каждый, аутентификация должна быть доступна всем.
                                .anyRequest().authenticated() // Прочие url для любого авторизированного юзверя.
                )
                .formLogin(
                        fl -> fl
                                .loginPage("/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .successHandler(successUserHandler) // Передам класс реализующий логику перенаправление после успешной аутентификации.
                                .permitAll()
                )
                .logout(
                        lo -> lo
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                );
        return http.build();
    }
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}