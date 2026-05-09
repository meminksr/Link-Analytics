package com.meminksr.linkanalytics.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "click_events")
@Getter
@Setter
@NoArgsConstructor
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hangi linke tıklandı? (ManyToOne ilişkisi - Bir linkin binlerce tıklaması olabilir)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", nullable = false)
    private Link link;

    @Column(nullable = false, updatable = false)
    private LocalDateTime clickTime;

    // Tıklayanın IP Adresi (İleride ülkeleri bulmak için kullanacağız)
    private String ipAddress;

    // Tarayıcı ve Cihaz Bilgisi (Mobilden mi girmiş, PC'den mi?)
    private String userAgent;

    @PrePersist
    protected void onCreate() {
        this.clickTime = LocalDateTime.now();
    }
}