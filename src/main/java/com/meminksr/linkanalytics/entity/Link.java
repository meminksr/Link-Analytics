package com.meminksr.linkanalytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "links")
@Getter
@Setter
@NoArgsConstructor
public class Link {

    // Bir link, tek bir kullanıcıya aittir (Many-To-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Uzun asıl URL (TEXT tipi uzun URL'ler için daha güvenlidir)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    //ürettiğimiz kısa kod (Örn: aB3x9). Aramalarda hız için index şart!
    @Column(nullable = false, unique = true, length = 15)
    private String shortCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Soft delete: Link silindiğinde veritabanından uçurmak yerine pasife çekeceğiz
    @Column(nullable = false)
    private boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Bir linkin birden fazla tıklanma verisi olabilir (One-To-Many)
    @OneToMany(mappedBy = "link", cascade = CascadeType.ALL)
    private List<ClickEvent> clickEvents;

    public Link orElseThrow(Object o) {
        return null;
    }
}