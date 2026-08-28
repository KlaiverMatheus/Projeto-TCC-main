package by.gabriel.gerenciadorEstoque.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean // Define um bean gerenciado pelo Spring
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Libera CORS para todos os endpoints da aplicação
                        // 1. Coloquei as duas origens juntas na mesma linha:
                        .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5173", "http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // Permite todos os cabeçalhos
                        .allowCredentials(true); // Permite envio de cookies/credenciais
            }
        };
    }
}