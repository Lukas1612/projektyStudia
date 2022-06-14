module projektTrzy {
    requires javafx.controls;
    requires javafx.fxml;

    opens projektTrzy to javafx.fxml;
    exports projektTrzy;
}