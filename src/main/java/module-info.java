module ua.parflare.sapr3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ua.parflare.sapr3 to javafx.fxml;
    exports ua.parflare.sapr3;
    exports ua.parflare.sapr3.controllers;
    opens ua.parflare.sapr3.controllers to javafx.fxml;
    exports ua.parflare.sapr3.utils;
    opens ua.parflare.sapr3.utils to javafx.fxml;
    exports ua.parflare.sapr3.models;
    opens ua.parflare.sapr3.models to javafx.fxml;
    exports ua.parflare.sapr3.algorithms;
    opens ua.parflare.sapr3.algorithms to javafx.fxml;
    exports ua.parflare.sapr3.algorithms.impl;
    opens ua.parflare.sapr3.algorithms.impl to javafx.fxml;
}