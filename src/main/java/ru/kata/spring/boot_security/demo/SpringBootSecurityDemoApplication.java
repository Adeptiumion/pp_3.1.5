package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kata.spring.boot_security.demo.util.Creator;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {
    private static Creator creator;
    @Autowired
    public SpringBootSecurityDemoApplication(Creator creator) {
        SpringBootSecurityDemoApplication.creator = creator;
    }
    public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
        creator.enrichAndCreateTables();
	}
}
