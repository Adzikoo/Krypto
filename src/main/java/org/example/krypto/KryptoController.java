package org.example.krypto;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class KryptoController {

    @FXML
    private TextField informationBlock;

    @FXML
    private TextField keyTextField;

    @FXML
    private TextField keyTextField2;

    @FXML
    private TextField keyTextField3;

    @FXML
    private TextArea inputTextField;

    @FXML
    private TextArea encryptedTextField;

    @FXML
    public void initialize() {
        inputTextField.setWrapText(true); // Włączenie zawijania tekstu
        encryptedTextField.setWrapText(true); // Włączenie zawijania tekstu
    }

    @FXML
    protected void onGenerateKeyClick() {
        informationBlock.setText("");
        keyTextField.setText(KeyGenerator.generateRandomKeyHex(16));
        keyTextField2.setText(KeyGenerator.generateRandomKeyHex(16));
        keyTextField3.setText(KeyGenerator.generateRandomKeyHex(16));
    }

    @FXML
    protected void onEncryptClick() {
        informationBlock.setText("");
        encryptedTextField.setText("");
        String plainText = inputTextField.getText();
        String keyHex1 = keyTextField.getText();
        String keyHex2 = keyTextField2.getText();
        String keyHex3 = keyTextField3.getText();

        if (!isValidHexKey(keyHex1) || !isValidHexKey(keyHex2) || !isValidHexKey(keyHex3)) {
            //przed wpisaniem czyść pole informacyjne

            informationBlock.setText("Błąd: Brak kluczy!");
            return;
        }
        if(plainText.isEmpty()) {
            informationBlock.setText("Błąd: Brak tekstu do szyfrowania!");
            return;
        }
        //wyswtlanie kluczy jako listy bajtów
        byte[] key1 = hexToBytes(keyHex1);
        byte[] key2 = hexToBytes(keyHex2);
        byte[] key3 = hexToBytes(keyHex3);
        System.out.println("Key 1: " + Arrays.toString(key1));
        System.out.println("Key 2: " + Arrays.toString(key2));
        System.out.println("Key 3: " + Arrays.toString(key3));

        // Tworzenie klucza TripleDES
        TripleDES.Key key = new TripleDES.Key(hexToBytes(keyHex1), hexToBytes(keyHex2), hexToBytes(keyHex3));

        // Szyfrowanie do Base64
        String encryptedBase64 = TripleDES.encryptToBase64(plainText.getBytes(StandardCharsets.UTF_8), key);

        encryptedTextField.setText(encryptedBase64);
    }

    @FXML
    protected void onDecryptClick() {
        informationBlock.setText("");
        inputTextField.setText("");
        String encryptedText = encryptedTextField.getText();
        String keyHex1 = keyTextField.getText();
        String keyHex2 = keyTextField2.getText();
        String keyHex3 = keyTextField3.getText();

        if (!isValidHexKey(keyHex1) || !isValidHexKey(keyHex2) || !isValidHexKey(keyHex3)) {
            informationBlock.setText("Błąd: Brak kluczy!");
            return;
        }
        if(encryptedText.isEmpty()) {
            informationBlock.setText("Błąd: Brak szyfrogramu!");
            return;
        }
        byte[] key1 = hexToBytes(keyHex1);
        byte[] key2 = hexToBytes(keyHex2);
        byte[] key3 = hexToBytes(keyHex3);
        System.out.println("Key 1: " + Arrays.toString(key1));
        System.out.println("Key 2: " + Arrays.toString(key2));
        System.out.println("Key 3: " + Arrays.toString(key3));

        // Tworzenie klucza TripleDES
        TripleDES.Key key = new TripleDES.Key(hexToBytes(keyHex1), hexToBytes(keyHex2), hexToBytes(keyHex3));

        // Deszyfrowanie Base64
        byte[] decryptedData = TripleDES.decryptFromBase64(encryptedText, key);
        String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);

        inputTextField.setText(decryptedText);
    }

    // Konwersja HEX -> bajty
    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    // Walidacja poprawności klucza (16 znaków HEX)
    private boolean isValidHexKey(String key) {
        return key != null && key.matches("[0-9A-Fa-f]{16}");
    }
}
