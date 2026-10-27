package org.ruralnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class RuralNetApplication {
  public static void main(String[] args) { SpringApplication.run(RuralNetApplication.class, args); }
  @Bean WebMvcConfigurer cors(@Value("${app.cors-origins}") String origins) {
    return new WebMvcConfigurer() { @Override public void addCorsMappings(CorsRegistry r) { r.addMapping("/api/**").allowedOrigins(origins.split(",")).allowedMethods("GET","POST","PATCH","DELETE","OPTIONS"); } };
  }
}
