module lual.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires io.netty.all;
    requires static lombok;
    opens github.lual to javafx.graphics;
}