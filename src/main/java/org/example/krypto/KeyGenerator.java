package org.example.krypto;

import java.security.SecureRandom;
import java.util.Arrays;

public class KeyGenerator {


    public static String generateRandomKeyHex() {
        byte[] key = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        return byteArrayToHexString(key);
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


}
