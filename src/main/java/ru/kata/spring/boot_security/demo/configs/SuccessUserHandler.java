package ru.kata.spring.boot_security.demo.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {

    private static final Logger handlerLogger = Logger.getLogger(SuccessUserHandler.class.getSimpleName());
    private final UserService userService;

    @Autowired
    public SuccessUserHandler(UserService userService) {
        this.userService = userService;
    }

    // Spring Security использует объект Authentication, пользователя авторизованной сессии.
    @Override
    public void onAuthenticationSuccess
    (
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_USER") && !roles.contains("ROLE_ADMIN")) {
            int id = userService.findByName(authentication.getName()).getId();
            response.sendRedirect("/user?id=" + id);
        } else {
            handlerLogger.info("Role is - ADMIN.");
            response.sendRedirect("/admin");
        }

    }

}