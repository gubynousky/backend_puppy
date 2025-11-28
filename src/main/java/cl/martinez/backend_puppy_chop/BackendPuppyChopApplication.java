package cl.martinez.backend_puppy_chop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendPuppyChopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendPuppyChopApplication.class, args);
        System.out.println("\n" +
                "═══════════════════════════════════════════════════════════\n" +
                "   🐕 PUPPYCHOP BACKEND - SERVIDOR INICIADO EXITOSAMENTE\n" +
                "═══════════════════════════════════════════════════════════\n" +
                "   📡 API REST: http://localhost:8080/api\n" +
                "   📊 Base de datos: PostgreSQL (puppychop_db)\n" +
                "   🌐 CORS habilitado para: http://localhost:3000\n" +
                "   ☕ Java 21 + Spring Boot 3.5.8\n" +
                "═══════════════════════════════════════════════════════════\n");
	}


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
