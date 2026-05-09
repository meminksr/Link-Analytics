package com.meminksr.linkanalytics.repository;

import com.meminksr.linkanalytics.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    // Spring Data JPA'nın sihirli kısmı:
    // Sadece metorkada SQL'e (SELECT COUNT... WHERE sdun adını İngilizce kurallara göre yazıyoruz, o ahort_code = ?) çeviriyor.
    boolean existsByShortCode(String shortCode);

    // içeride varmı diye kontrol
    // Kısa koda göre veritabanında arama yapar
    java.util.Optional<Link> findByShortCode(String shortCode);

}