package org.example.krypto;

import java.security.SecureRandom;
import java.util.Set;

public class KeyGenerator {


    public static String generateRandomKeyHex() {
        String hex;
        byte[] key = new byte[8];
        SecureRandom random = new SecureRandom();
        do{
            random.nextBytes(key);
            hex = byteArrayToHexString(key).toUpperCase();
        }
        while (isWeakKey(hex));
        return hex;
    }

    private static boolean isWeakKey(String hexKey) {
        Set<String> weakKeys = Set.of(
                "0101010101010101",
                "FEFEFEFEFEFEFEFE",
                "E0E0E0E0F1F1F1F1",
                "1F1F1F1F0E0E0E0E",
                "0000000000000000",
                "FFFFFFFFFFFFFFFF",
                "E1E1E1E1F0F0F0F0",
                "1E1E1E1E0F0F0F0F",
                "011F011F010E010E",
                "1F011F010E010E01",
                "01E001E001F101F1",
                "E001E001F101F101",
                "01FE01FE01FE01FE",
                "FE01FE01FE01FE01",
                "1FE01FE00EF10EF1",
                "E01FE01FF10EF10E",
                "1FFE1FFE0EFE0EFE",
                "FE1FFE1FFE0EFE0E",
                "E0FEE0FEF1FEF1FE",
                "FEE0FEE0FEF1FEF1"

        );

        return weakKeys.contains(hexKey);
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


}
