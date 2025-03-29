package org.example.krypto;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class KryptoController {

    @FXML
    private TextField keyTextField;

    @FXML
    protected void onGenerateKeyClick() {
        String generatedKey = KeyGenerator.generateRandomKeyHex(); // Wywołanie generatora
        keyTextField.setText(generatedKey);
    }
}
