module com.example.airlinesautomaticsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.airlinesautomaticsystem to javafx.fxml;
    exports com.example.airlinesautomaticsystem;
}