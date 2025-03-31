module org.example.krypto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.krypto to javafx.fxml;
    exports org.example.krypto;
}