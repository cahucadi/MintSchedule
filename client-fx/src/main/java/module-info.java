module com.mint.clientfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.datafaker;
    requires org.apache.commons.lang3;

    opens com.mint.clientfx to javafx.fxml;
    exports com.mint.clientfx;

    opens com.mint.clientfx.controller to javafx.fxml;
    exports com.mint.clientfx.controller;
    exports com.mint.clientfx.model;
    exports com.mint.clientfx.util;
    exports com.mint.clientfx.view;
    opens com.mint.clientfx.view to javafx.fxml;
}