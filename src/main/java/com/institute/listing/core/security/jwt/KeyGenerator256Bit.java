package com.institute.listing.core.security.jwt;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator256Bit {
    public static String generateDynamicSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);

            SecretKey secretKey = keyGenerator.generateKey();

            byte[] keyBytes = secretKey.getEncoded();

            return bytesToHex(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating dynamic secret key", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}