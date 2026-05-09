package com.meminksr.linkanalytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
// MSSQL'deki tablonun adı 'users' olacak (user kelimesi SQL'de bazen rezerve kelime olduğu için 'users' kullanmak daha iyidir)
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'yi MSSQL otomatik artırsın (Auto Increment)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String apiKey;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Veritabanına ilk kaydedilmeden hemen önce çalışma zamanını otomatik atar
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
