package org.eriksandsten;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import java.nio.charset.StandardCharsets;

@Configuration
@SpringBootApplication
public class MediaServerApplication {
    static {
        System.loadLibrary("wHelper");
    }

    public static void main(String[] args) {
        SpringApplication.run(MediaServerApplication.class, args);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());

        return messageSource;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setOrder(0);
        resolver.setCacheable(false);
        resolver.setCheckExistence(true);

        return resolver;
    }
}
