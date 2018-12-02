module lual.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    requires io.netty.all;
    requires com.google.common;
    requires static lombok;
    opens github.lual to javafx.graphics, com.google.common;
    opens github.lual.util to javafx.graphics, com.google.common;
    opens github.lual.view to javafx.graphics, javafx.fxml, com.google.common;
    opens github.lual.messages.base to javafx.graphics, com.google.common;
    opens github.lual.messages.types to javafx.graphics, com.google.common;
}