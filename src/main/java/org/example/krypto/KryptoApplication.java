package org.example.krypto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

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
        //launch();
//        DES.Key key1 = new DES.Key("01234567".getBytes());
//        DES.Key key2 = new DES.Key("87654321".getBytes());
//        DES.Key key3 = new DES.Key("43215678".getBytes());
//
//        String s = "A";



//        //szyfruj tekst
//        byte[] szyfr1 = DES.encrypt(s.getBytes(), key1);
//        byte[] odszyfr1 = DES.decrypt(szyfr1, key2);
//        byte[] szyfr2 = DES.encrypt(odszyfr1, key3);
//        //odszyfruj tekst
//        byte[] odszyfr2 = DES.decrypt(szyfr2, key3);
//        byte[] szyfr3 = DES.encrypt(odszyfr2, key2);
//        byte[] odszyfr3 = DES.decrypt(szyfr3, key1);

//





//        // wypisz tekst jawny
//        System.out.println("Tekst jawny: " + s);
//        //wypisz klucz w postaci hex
//        //System.out.println("Klucz 1: " + key.getBits());
//        // wypisz tekst zaszyfrowany
//        System.out.println("Tekst zaszyfrowany: " + new String(szyfr1));
//        // wypisz tekst odszyfrowany
//        System.out.println("Tekst odszyfrowany: " + new String(odszyfr1));
//        // wypisz tekst zasztfrowany
//        System.out.println("Tekst zaszyfrowany: " + new String(szyfr2));
//        // wypisz tekst odszyfrowany
//        System.out.println("Tekst odszyfrowany: " + new String(odszyfr2));
//        // wypisz tekst zaszyfrowany
//        System.out.println("Tekst zaszyfrowany: " + new String(szyfr3));
//        // wypisz tekst odszyfrowany
//        System.out.println("Tekst odszyfrowany: " + new String(odszyfr3));


//        //szyfruj tekst
//        byte[] szyfr1 = DES.encrypt(s.getBytes(), key1);
//        //deszyfruj tekst
//
//        byte[] odszyfr1 = DES.decrypt(szyfr1, key1);
//        //wypisz tekst jawny
//        System.out.println("Tekst jawny: " + s);
//        //wypisz klucz w postaci hex
//        //System.out.println("Klucz 1: " + key.getBits());
//        // wypisz tekst zaszyfrowany
//        System.out.println("Tekst zaszyfrowany: " + new String(szyfr1));
//        // wypisz tekst odszyfrowany
//        System.out.println("Tekst odszyfrowany: " + new String(odszyfr1));

// Define three keys
        byte[] key1 = "01234567".getBytes();
        byte[] key2 = "87654321".getBytes();
        byte[] key3 = "43215678".getBytes();

        // Create a TripleDES.Key object
        TripleDES.Key tripleKey = new TripleDES.Key(key1, key2, key3);

        // Sample text to encrypt and decrypt
        String text = "HelHello";
        byte[] textBytes = text.getBytes();

        // Encrypt the text
        byte[] encryptedData = TripleDES.encrypt3D(textBytes, tripleKey);

        // Decrypt the text
        byte[] decryptedData = TripleDES.decrypt3D(encryptedData, tripleKey);

        // Print the results
        System.out.println("Original text: " + text);
        System.out.println("Encrypted text: " + Arrays.toString(encryptedData));
        System.out.println("Decrypted text: " + new String(decryptedData));






    }
}

