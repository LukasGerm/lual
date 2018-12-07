package github.lual;

import com.fazecast.jSerialComm.SerialPort;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.tulskiy.keymaster.common.Provider;
import github.lual.messages.types.ClientAlarmMessage;
import github.lual.messages.types.ServerAlarmMessage;
import github.lual.net.TlsClient;
import github.lual.util.ComponentManager;
import github.lual.util.ResourceLoader;
import github.lual.view.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.swing.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    private static final String KEYSTROKE_HOTKEY = "control alt 0";
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    private NotificationStage notificationStage;

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
        Configuration config = Configuration.getInstance();
        ResourceBundle resourceBundle = ResourceLoader.getInstance().getResourceBundle(config.getResourceBundleLanguage());
        notificationStage = new NotificationStage();
        notificationStage.showInBackground();

        final EventBus eventBus = new EventBus();
        eventBus.register(this);
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
        registerHotkey(eventBus);

        // close-event required to disconnect client and cleanup resources
        stage.setOnCloseRequest(event -> {
            try {
                notificationStage.close();
                client.close();
            } catch (IOException e) {
            }
        });
    }

    private void registerHotkey(EventBus eventBus) {
        Provider provider = Provider.getCurrentProvider(false);
        provider.register(KeyStroke.getKeyStroke(KEYSTROKE_HOTKEY), hotKey -> {
            eventBus.post(new ClientAlarmMessage());
        });
    }

    private void loadComponents(EventBus eventBus) {
        new LoginView(eventBus);
        new MainView(eventBus);
        new ChangePasswordView(eventBus);
    }

    @Subscribe
    private void onAlarm(ServerAlarmMessage message) {
        ResourceBundle resourceBundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
        Platform.runLater(() -> {
            Notifications.create() //
                    .title(resourceBundle.getString("AlarmTitle")) //
                    .text(String.format(resourceBundle.getString("AlarmText"), message.getFirstName(), message.getLastName(), message.getRoom())) //
                    .owner(notificationStage) //
                    .position(Pos.BOTTOM_RIGHT) //
                    .hideAfter(Duration.INDEFINITE) //
                    .showWarning();
        });
    }

    private void jSerialTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            SerialPort[] serialPorts = SerialPort.getCommPorts();
            if (serialPorts.length < 1) {
                Platform.runLater(() -> {
                    Alerts.error("No serial port", "No serial port found.", false);
                });
                return;
            }
            for (int i = 0; i < serialPorts.length; i++) {
                final int ix = i;
                Platform.runLater(() -> {
                    Alerts.info("COM-Port " + ix, "COM-Port Index" + ix + " => " + serialPorts[ix].getDescriptivePortName() + " / " + serialPorts[ix].getPortDescription() + " / " + serialPorts[ix].getSystemPortName(), false );
                });
            }
            SerialPort serialPort = serialPorts[0];
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
            serialPort.openPort();
            try {
                while(true) {
                    int bytesAvailable = serialPort.bytesAvailable();
                    while (bytesAvailable == 0) {
                        Thread.sleep(20);
                    }

                    if (bytesAvailable < 0) {
                        Platform.runLater(() -> {
                            Alerts.info("SerialPort result", "<empty>", false);
                        });
                    } else {
                        byte[] readBuffer = new byte[bytesAvailable];
                        int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                        String hex = String.format("%040x", new BigInteger(1, readBuffer));
                        Platform.runLater(() -> {
                            Alerts.info("SerialPort result", hex, false);
                        });
                    }
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Alerts.exception(e);
                });
            }
        });
    }
}
