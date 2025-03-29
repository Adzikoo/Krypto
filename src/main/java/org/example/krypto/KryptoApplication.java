package org.example.krypto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import static org.example.krypto.Bits.hexToBytes;

public class KryptoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KryptoApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setMinHeight(scene.getHeight());
        stage.setMinWidth(scene.getWidth());
        stage.show();
    }


    public static void main(String[] args) {
        launch();

 //Define three keys
        // Klucze w formacie HEX
        String hexKey1 = "0123456789ABCDEF";
        String hexKey2 = "FEDCBA9876543210";
        String hexKey3 = "A1B2C3D4E5F60789";

        // Konwersja HEX -> bajty
        byte[] key1 = hexToBytes(hexKey1);
        byte[] key2 = hexToBytes(hexKey2);
        byte[] key3 = hexToBytes(hexKey3);
        // Create a TripleDES.Key object
        TripleDES.Key tripleKey = new TripleDES.Key(key1, key2, key3);

        // Sample text to encrypt and decrypt
        String text = "HelHello";
        byte[] textBytes = text.getBytes();

        // Encrypt the text
        byte[] encryptedData = TripleDES.encrypt3D(textBytes, tripleKey);

//        // Encode encrypted data as Base64
//        String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedData);
//
//        // Decrypt the text
//        byte[] decryptedData = TripleDES.decrypt3D(encryptedData, tripleKey);
//
//        // Print the results
//        System.out.println("Original text: " + text);
//        System.out.println("Encrypted text (Base64): " + encryptedBase64);
//        System.out.println("Decrypted text: " + new String(decryptedData));


        // Szyfrowanie do Base64
        String encryptedBase64 = TripleDES.encryptToBase64(textBytes, tripleKey);
        System.out.println("Encrypted (Base64): " + encryptedBase64);

        // Deszyfrowanie z Base64
        String decryptedText = new String(TripleDES.decryptFromBase64(encryptedBase64, tripleKey));
        System.out.println("Decrypted: " + decryptedText);






    }
}

