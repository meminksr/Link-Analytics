package com.meminksr.linkanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class LinkAnalyticsApplication {

    // 1. METOD: Ana çalışma metodu
    public static void main(String[] args) {
        SpringApplication.run(LinkAnalyticsApplication.class, args);
    }

    @Bean
    public org.springframework.boot.CommandLineRunner initDatabase(com.meminksr.linkanalytics.repository.UserRepository userRepository) {
        return args -> {
            // Eğer veritabanında hiç kullanıcı yoksa, test için bir tane ekle
            if (userRepository.count() == 0) {
                com.meminksr.linkanalytics.entity.User testUser = new com.meminksr.linkanalytics.entity.User();
                testUser.setEmail("mehmet@yildiz.edu.tr");
                testUser.setApiKey("super-gizli-api-key");
                userRepository.save(testUser);
                System.out.println("--- Test User Added to the Database! ---");
            }
        };
    }
}