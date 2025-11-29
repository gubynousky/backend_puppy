package cl.martinez.backend_puppy_chop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendPuppyChopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendPuppyChopApplication.class, args);
        System.out.println("\n🐕 PUPPYCHOP BACKEND INICIADO - http://localhost:8080/api\n");
    }
}