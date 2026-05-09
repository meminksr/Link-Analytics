package com.meminksr.linkanalytics.repository;

import com.meminksr.linkanalytics.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
    // Belirli bir linkin (ID'sine göre) toplam kaç kez tıklandığını sayar
    long countByLinkId(Long linkId);
}
