package github.lual;

import com.fazecast.jSerialComm.SerialPort;
import com.google.common.eventbus.EventBus;
import github.lual.net.TlsClient;
import github.lual.util.ComponentManager;
import github.lual.util.ResourceLoader;
import github.lual.view.Alerts;
import github.lual.view.LoginView;
import github.lual.view.MainView;
import github.lual.view.ShowComponentEvent;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ResourceBundle;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    public static void main(String[] args) {
        String filePath = Thread.currentThread().getContextClassLoader().getResource("server.pfx").getFile();
        System.setProperty("javax.net.ssl.keyStore", filePath);
        System.setProperty("javax.net.ssl.keyStorePassword", "changeme");
        System.setProperty("javax.net.ssl.trustStore", filePath);
        System.setProperty("javax.net.ssl.trustStorePassword", "changeme");
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        jSerialTest();
        Configuration config = Configuration.getInstance();
        ResourceBundle resourceBundle = ResourceLoader.getInstance().getResourceBundle(config.getResourceBundleLanguage());
        final EventBus eventBus = new EventBus();
        final ComponentManager componentManager = new ComponentManager(stage, eventBus);
        final TlsClient client = new TlsClient(config.getHost(), config.getPort());
        final EventBusMessageGateway messageGateway = new EventBusMessageGateway(eventBus, client);

        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setTitle(resourceBundle.getString("AppTitle"));
        loadComponents(eventBus);

        // show the window
        stage.show();

        // connect the client
        try {
            client.connect();
        } catch (Exception e) {
            Alerts.exception(e);
            System.exit(1);
        }

        eventBus.post(ShowComponentEvent.of(LoginView.class));

        // close-event required to disconnect client and cleanup resources
        stage.setOnCloseRequest(event -> {
            try {
                client.close();
            } catch (IOException e) {
            }
        });
    }

    private void loadComponents(EventBus eventBus) {
        new LoginView(eventBus);
        new MainView(eventBus);
    }

    private void jSerialTest() {
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        if (serialPorts.length < 1) {
            Alerts.error("No serial port", "No serial port found.", false);
            return;
        }
        SerialPort serialPort = serialPorts[0];
        serialPort.openPort();
        try {
            while(true) {
                while (serialPort.bytesAvailable() == 0) {
                    Thread.sleep(20);
                }

                byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                String hex = String.format("%040x", new BigInteger(1, readBuffer));
                Alerts.info("SerialPort result", hex, false);
            }
        } catch (Exception e) {
            Alerts.exception(e);
        }
    }
}
