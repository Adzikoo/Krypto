package org.example.krypto;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class TripleDES {
    public static class Key {
        private DES.Key key1;
        private DES.Key key2;
        private DES.Key key3;

        public Key(byte[] key1, byte[] key2, byte[] key3) {
            this.key1 = new DES.Key(key1);
            this.key2 = new DES.Key(key2);
            this.key3 = new DES.Key(key3);
        }

        public DES.Key getKey1() {
            return key1;
        }

        public DES.Key getKey2() {
            return key2;
        }

        public DES.Key getKey3() {
            return key3;
        }
    }

    public static byte[] encrypt3D(byte[] data, Key key) {
        byte[] encryptedData = DES.encrypt(data, key.getKey1());
        byte[] decryptedData = DES.decrypt(encryptedData, key.getKey2());
        return DES.encrypt(decryptedData, key.getKey3());
    }

    public static byte[] decrypt3D(byte[] data, Key key) {
        byte[] decryptedData = DES.decrypt(data, key.getKey3());
        byte[] encryptedData = DES.encrypt(decryptedData, key.getKey2());
        return DES.decrypt(encryptedData, key.getKey1());
    }

    // Szyfrowanie i zwracanie Base64
    public static String encryptToBase64(byte[] data, Key key) {
        byte[] encryptedData = encrypt3D(data, key);
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // Deszyfrowanie z Base64
    public static byte[] decryptFromBase64(String base64Data, Key key) {
        byte[] encryptedData = Base64.getDecoder().decode(base64Data);
        return decrypt3D(encryptedData, key);
    }
}