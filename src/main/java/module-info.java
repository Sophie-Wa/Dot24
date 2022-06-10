module com.example.dot24 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.dot24 to javafx.fxml;
    exports com.example.dot24;
}