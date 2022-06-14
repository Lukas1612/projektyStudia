module sample {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.base;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires jdk.jsobject;
    requires java.sql;


    opens sample to javafx.fxml;
    exports sample;
}