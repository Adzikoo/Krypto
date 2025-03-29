module org.example.krypto {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.krypto to javafx.fxml;
    exports org.example.krypto;
}