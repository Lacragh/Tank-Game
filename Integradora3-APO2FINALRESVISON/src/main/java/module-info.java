module com.example.integradora3apo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.integradora3apo to javafx.fxml;
    exports com.example.integradora3apo;
    exports com.example.integradora3apo.control;
    opens com.example.integradora3apo.control to javafx.fxml;
}