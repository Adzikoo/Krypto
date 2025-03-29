package org.example.krypto;

public class TripleDES {

    public static byte[] encrypt3DES(byte[] message, DES.Key key1, DES.Key key2, DES.Key key3) {
        byte[] result = DES.encrypt(message, key1);
        result = DES.decrypt(result, key2);
        result = DES.encrypt(result, key3);
        return result;
    }
    public static byte[] decrypt3DES(byte[] cipher, DES.Key key1, DES.Key key2, DES.Key key3) {
        byte[] result = DES.decrypt(cipher, key3);
        result = DES.encrypt(result, key2);
        result = DES.decrypt(result, key1);
        return result;
    }
}
