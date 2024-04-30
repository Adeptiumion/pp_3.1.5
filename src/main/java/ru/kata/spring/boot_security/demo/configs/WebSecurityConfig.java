package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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
                .authorizeHttpRequests
                        (
                                auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN") // На "админку" попадет только админ.
                                        .requestMatchers("/login").permitAll() // Залогиниться может каждый, аутентификация должна быть доступна всем.
                                        .anyRequest().hasAnyRole("ADMIN", "USER") // Прочие url для любого авторизированного юзверя.
                        )
                .formLogin
                        (
                                fl -> fl
                                        .successHandler(successUserHandler)
                                        .permitAll()
                        )
                .logout
                        (
                                lo -> lo
                                        .logoutUrl("/logout")
                                        .logoutSuccessUrl("/login")
                        );
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    /*// аутентификация inMemory
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("user")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }*/
}