module com.mint.clientfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.mint.clientfx to javafx.fxml;
    exports com.mint.clientfx;
}