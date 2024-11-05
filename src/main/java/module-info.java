module ua.parflare.sapr3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ua.parflare.sapr3 to javafx.fxml;
    exports ua.parflare.sapr3;
}