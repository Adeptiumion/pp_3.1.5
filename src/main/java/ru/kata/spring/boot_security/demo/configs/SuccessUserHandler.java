package ru.kata.spring.boot_security.demo.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {

    private static final Logger handlerLogger = Logger.getLogger(SuccessUserHandler.class.getSimpleName());
    private final UserRepository userRepository;

    public SuccessUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security использует объект Authentication, пользователя авторизованной сессии.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_USER") && !roles.contains("ROLE_ADMIN")) {
            int id = Objects.requireNonNull(userRepository.findByName(authentication.getName()).orElse(null)).getId();
            response.sendRedirect("/user?id=" + id);
        } else if (roles.contains("ROLE_ADMIN")) {
            handlerLogger.info("Role is - ADMIN.");
            response.sendRedirect("/admin");
        } else {
            handlerLogger.warning("USER - 404. Incorrect authentication data!");
            response.sendRedirect("/login");
        }

    }

}