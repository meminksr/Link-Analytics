package com.meminksr.linkanalytics.controller;

import com.meminksr.linkanalytics.entity.Link;
import com.meminksr.linkanalytics.entity.User;
import com.meminksr.linkanalytics.repository.ClickEventRepository;
import com.meminksr.linkanalytics.repository.LinkRepository;
import com.meminksr.linkanalytics.repository.UserRepository;
import com.meminksr.linkanalytics.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Bu sınıfın bir REST API olduğunu belirtir
@RequestMapping("/api/v1/links") // Dışarıdan bu adrese istek atılacak
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final ClickEventRepository clickEventRepository;


    // DTO (Veri Taşıma Objesi): Dışarıdan sadece 'originalUrl' verisini JSON olarak alacağız
    public record LinkCreateRequest(String originalUrl) {
    }

    // POST isteği ile yeni link oluşturma ucu
    @PostMapping
    public ResponseEntity<String> createShortLink(@RequestBody LinkCreateRequest request) {

        // 1. Şimdilik sistemdeki ilk kullanıcıyı (1 ID'li test kullanıcımızı) buluyoruz
        User testUser = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // 2. Servis katmanına "al bu uzun linki, bana kısa kod üret"
        Link savedLink = linkService.createShortLink(request.originalUrl(), testUser);

        // 3. Oluşturulan o eşsiz kısa linki kullanıcıya cevap (Response) olarak dönüyoruz
        String shortUrl = "http://localhost:8080/api/v1/links/" + savedLink.getShortCode();
        return ResponseEntity.ok(shortUrl);

    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortCode, jakarta.servlet.http.HttpServletRequest request) {
        Link link = linkService.getLinkByShortCode(shortCode);

        // 1. Kullanıcının IP adresi ve User-Agent bilgisini al
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // 2. Arka planda kaydetme emrini ver (Asenkron olduğu için beklemeden alt satıra geçer!)
        linkService.recordClickEvent(link, ip, userAgent);

        // 3. Kullanıcıyı hiç bekletmeden fırlat
        return ResponseEntity.status(org.springframework.http.HttpStatus.FOUND)
                .location(java.net.URI.create(link.getOriginalUrl()))
                .build();
    }

    // Dışarıya analitik verilerini döneceğimiz paket (DTO)
    public record AnalyticsResponse(String originalUrl, String shortCode, long totalClicks) {
    }

    // GET isteği: Kısa kodun sonuna "/stats" eklenince bu metod çalışacak
    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<AnalyticsResponse> getLinkStatistics(@PathVariable String shortCode) {

        // 1. Önce linki bul (Yine Cache'den yararlanıyoruz, çok hızlı bulacak!)
        Link link = linkService.getLinkByShortCode(shortCode);

        // 2. Bu linke ait tıklama sayısını veritabanından çek
        long clickCount = clickEventRepository.countByLinkId(link.getId());

        // 3. Sonuçları DTO paketine koy ve kullanıcıya JSON olarak dön
        AnalyticsResponse response = new AnalyticsResponse(
                link.getOriginalUrl(),
                link.getShortCode(),
                clickCount
        );

        return ResponseEntity.ok(response);
    }

    // ALLAH BÜYÜKTÜR
}