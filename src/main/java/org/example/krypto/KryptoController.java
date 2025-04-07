package org.example.krypto;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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
    private RadioButton radioText;

    @FXML
    private RadioButton radioFile;

    @FXML
    private TextField filePathEncrypt, filePathDecrypt;

    private FileChooser fileChooser = new FileChooser();


    @FXML
    public void initialize() {
        inputTextField.setWrapText(true);
        encryptedTextField.setWrapText(true);

        // Obsługa przeciągania pliku do pola filePathEncrypt
        filePathEncrypt.setOnDragOver(event -> {
            if (event.getGestureSource() != filePathEncrypt && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        filePathEncrypt.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File file = db.getFiles().get(0); // pierwszy plik
                filePathEncrypt.setText(file.getAbsolutePath());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        // Obsługa przeciągania pliku do pola filePathDecrypt
        filePathDecrypt.setOnDragOver(event -> {
            if (event.getGestureSource() != filePathDecrypt && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.COPY);
            }
            event.consume();
        });

        filePathDecrypt.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File file = db.getFiles().get(0); // pierwszy plik
                filePathDecrypt.setText(file.getAbsolutePath());
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
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
        if (radioText.isSelected()) {
            encryptText();
        } if (radioFile.isSelected()){
            onEncryptFileClick();
        }
    }

    @FXML
    protected void onDecryptClick() {
        if (radioText.isSelected()) {
            decryptText();
        } if (radioFile.isSelected()) {
            onDecryptFileClick();
        }
    }

    @FXML
    protected void encryptText() {
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

//        // Szyfrowanie do Base64
//        String encryptedBase64 = TripleDES.encryptToBase64(plainText.getBytes(StandardCharsets.UTF_8), key);
//
//        encryptedTextField.setText(encryptedBase64);

        byte[] encryptedBytes = TripleDES.encrypt3D(plainText.getBytes(StandardCharsets.UTF_8), key);
        String encryptedHex = bytesToHex(encryptedBytes);
        encryptedTextField.setText(encryptedHex);
        informationBlock.setText("Sukces: Szyfrowanie zakończone.");
    }


    @FXML
    protected void decryptText() {
        informationBlock.setText("");
        inputTextField.setText("");
        String encryptedText = encryptedTextField.getText();
        String encryptedHex = encryptedTextField.getText();
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

//        // Deszyfrowanie Base64
//        byte[] decryptedData = TripleDES.decryptFromBase64(encryptedText, key);
//        String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
//
//        inputTextField.setText(decryptedText);
        byte[] encryptedBytes = hexToBytes(encryptedHex);
        byte[] decryptedBytes = TripleDES.decrypt3D(encryptedBytes, key);
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
        inputTextField.setText(decryptedText);
        informationBlock.setText("Sukces: Deszyfrowanie zakończone.");
    }

    @FXML
    protected void onLoadFileClick() {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            filePathEncrypt.setText(file.getAbsolutePath());
        }
    }

    @FXML
    protected void onLoadFileDecryptClick() {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            filePathDecrypt.setText(file.getAbsolutePath());
        }
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



    @FXML
    protected void onSaveKeysClick() {
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try (FileWriter writer = new FileWriter(selectedFile)) {
                writer.write(keyTextField.getText() + "\n");
                writer.write(keyTextField2.getText() + "\n");
                writer.write(keyTextField3.getText() + "\n");
                informationBlock.setText("Klucze zostały zapisane.");
            } catch (IOException e) {
                informationBlock.setText("Błąd zapisu kluczy.");
            }
        }
    }
    @FXML
    protected void onLoadKeysClick() {
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                String[] keys = content.split("\n");
                if (keys.length == 3) {
                    keyTextField.setText(keys[0].trim());
                    keyTextField2.setText(keys[1].trim());
                    keyTextField3.setText(keys[2].trim());
                    informationBlock.setText("Klucze zostały załadowane.");
                } else {
                    informationBlock.setText("Błąd: Niepoprawny format pliku z kluczami.");
                }
            } catch (IOException e) {
                informationBlock.setText("Błąd odczytu pliku.");
            }
        }
    }


    @FXML
    protected void onEncryptFileClick() {
        String filePath = filePathEncrypt.getText();
        if (filePath.isEmpty()) {
            informationBlock.setText("Błąd: Brak ścieżki do pliku!");
            return;
        }

        File selectedFile = new File(filePath);
        if (!selectedFile.exists()) {
            informationBlock.setText("Błąd: Plik nie istnieje!");
            return;
        }

        try {
            byte[] fileBytes = Files.readAllBytes(selectedFile.toPath());

            String keyHex1 = keyTextField.getText();
            String keyHex2 = keyTextField2.getText();
            String keyHex3 = keyTextField3.getText();

            if (!isValidHexKey(keyHex1) || !isValidHexKey(keyHex2) || !isValidHexKey(keyHex3)) {
                informationBlock.setText("Błąd: Brak kluczy!");
                return;
            }

            byte[] keyBytes1 = hexToBytes(keyHex1);
            byte[] keyBytes2 = hexToBytes(keyHex2);
            byte[] keyBytes3 = hexToBytes(keyHex3);

            TripleDES.Key key = new TripleDES.Key(keyBytes1, keyBytes2, keyBytes3);

            byte[] encryptedData = TripleDES.encrypt3D(fileBytes, key);

            Files.write(selectedFile.toPath(), encryptedData);

            informationBlock.setText("Plik został zaszyfrowany.");
        } catch (IOException e) {
            informationBlock.setText("Błąd odczytu/zapisu pliku.");
        }
        informationBlock.setText("Sukces: Szyfrowanie zakończone.");
    }

    @FXML
    protected void onDecryptFileClick() {
        String filePath = filePathDecrypt.getText();
        if (filePath.isEmpty()) {
            informationBlock.setText("Błąd: Brak ścieżki do pliku!");
            return;
        }

        File selectedFile = new File(filePath);
        if (!selectedFile.exists()) {
            informationBlock.setText("Błąd: Plik nie istnieje!");
            return;
        }

        try {
            byte[] fileBytes = Files.readAllBytes(selectedFile.toPath());

            String keyHex1 = keyTextField.getText();
            String keyHex2 = keyTextField2.getText();
            String keyHex3 = keyTextField3.getText();

            if (!isValidHexKey(keyHex1) || !isValidHexKey(keyHex2) || !isValidHexKey(keyHex3)) {
                informationBlock.setText("Błąd: Brak kluczy!");
                return;
            }

            byte[] keyBytes1 = hexToBytes(keyHex1);
            byte[] keyBytes2 = hexToBytes(keyHex2);
            byte[] keyBytes3 = hexToBytes(keyHex3);

            TripleDES.Key key = new TripleDES.Key(keyBytes1, keyBytes2, keyBytes3);

            byte[] decryptedData = TripleDES.decrypt3D(fileBytes, key);

            Files.write(selectedFile.toPath(), decryptedData);

            informationBlock.setText("Plik został odszyfrowany.");
        } catch (IOException e) {
            informationBlock.setText("Błąd odczytu/zapisu pliku.");
        }
        informationBlock.setText("Sukces: Deszyfrowanie zakończone.");
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }



}
