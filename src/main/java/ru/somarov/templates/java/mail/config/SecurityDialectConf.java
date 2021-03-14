package ru.somarov.templates.java.mail.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
@Configuration
public class SecurityDialectConf {
        @Bean
        public SpringSecurityDialect springSecurityDialect(){
            return new SpringSecurityDialect();

    }
}
