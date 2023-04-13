module com.example.gobacknarq {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gobacknarq to javafx.fxml;
    exports com.example.gobacknarq;
}