package com.meminksr.linkanalytics.util;

import java.security.SecureRandom;

public class ShortCodeGenerator {

    // Havuzumuz Kullanabileceğimiz 62 karakter (Base62)
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // Normal 'Random' yerine 'SecureRandom' kullanıyoruz.
    // Çünkü tahmin edilmesi çok daha zor, kriptografik olarak güvenli şifreler/kodlar üretir.
    private static final SecureRandom random = new SecureRandom();

    //  istediğimiz uzunlukta (örneğin 6) rastgele bir kod üretecek metod
    public static String generate(int length) {

        // String yerine StringBuilder kullanıyoruz.
        // Çünkü döngü içinde sürekli yeni karakter eklerken belleği (RAM) şişirmemesi lazım.
        StringBuilder shortCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Havuzun uzunluğu (62) içinden rastgele bir sayı çek
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());

            // O sıradaki karakteri alıp StringBuilder'a ekle
            shortCode.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }

        return shortCode.toString();
    }
}