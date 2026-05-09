package com.meminksr.linkanalytics.service;

import com.meminksr.linkanalytics.entity.ClickEvent;
import com.meminksr.linkanalytics.entity.Link;
import com.meminksr.linkanalytics.entity.User;
import com.meminksr.linkanalytics.repository.ClickEventRepository;
import com.meminksr.linkanalytics.repository.LinkRepository;
import com.meminksr.linkanalytics.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final ClickEventRepository clickEventRepository;
    private final LinkRepository linkRepository;

    // Uzun linki alıp, kısa kodlu Link nesnesini oluşturan ana metod
    public Link createShortLink(String originalUrl, User user) {

        String generatedCode;

        // Üretilen kod veritabanında varsa, yeni bir tane üretmeye devam et.
        // Yoksa döngüden çık ve o eşsiz kodu kullan.
        do {
            generatedCode = ShortCodeGenerator.generate(6);
        } while (linkRepository.existsByShortCode(generatedCode));

        //  yeni Link objesi
        Link newLink = new Link();
        newLink.setOriginalUrl(originalUrl);
        newLink.setShortCode(generatedCode);
        newLink.setUser(user);
        // createdAt ve isActive zaten Entity sınıfında otomatik ayarlanıyor

        // Veritabanına kaydet ve geri döndür
        return linkRepository.save(newLink);
    }

    @org.springframework.scheduling.annotation.Async
    public void recordClickEvent(Link link, String ipAddress, String userAgent) {

        ClickEvent event = new ClickEvent();
        event.setLink(link);
        event.setIpAddress(ipAddress);
        event.setUserAgent(userAgent); // Tarayıcı, telefon, PC bilgisi vs.

        clickEventRepository.save(event);

        System.out.println("A click was recorded in the background! IP: " + ipAddress);
    }

    // Kısa kodu veritabanında arar.
    // Ancak @Cacheable sayesinde, aynı 'shortCode' ile ikinci bir istek gelirse
    // bu metodun İÇİNE HİÇ GİRMEZ, sonucu doğrudan RAM'den (bellekten) geri döner!
    @org.springframework.cache.annotation.Cacheable(value = "links", key = "#shortCode")
    public Link getLinkByShortCode(String shortCode) {

        // Bu log (yazı) sadece veritabanına gerçekten gidildiğinde konsolda görünecek.
        System.out.println("--- NOT FOUND IN CACHE! Accessing the database (MSSQL): " + shortCode + " ---");

        return linkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("No such short link was found!"));
    }
}