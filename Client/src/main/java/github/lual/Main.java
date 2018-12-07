package github.lual;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    private static final String KEYSTROKE_HOTKEY = "control alt 0";
    public static final int WINDOW_WIDTH = 400;
    public static final int WINDOW_HEIGHT = 300;

    private NotificationStage notificationStage;
    private Stage stage;

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
        this.stage = stage;
        Configuration config = Configuration.getInstance();
        ResourceBundle resourceBundle = ResourceLoader.getInstance().getResourceBundle(config.getResourceBundleLanguage());

        // app should be started only once
        try {
            SingleInstanceLock.getInstance().lock();
        } catch (Exception e) {
            Alerts.exception(e);
            System.exit(0);
        }

        notificationStage = new NotificationStage();
        notificationStage.showInBackground();

        final EventBus eventBus = new EventBus();
        eventBus.register(this);
        new Tray(stage, eventBus);
        final ComponentManager componentManager = new ComponentManager(stage, eventBus);
        final TlsClient client = new TlsClient(config.getHost(), config.getPort());
        final EventBusMessageGateway messageGateway = new EventBusMessageGateway(eventBus, client);

        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setTitle(resourceBundle.getString("AppTitle"));
        stage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                stage.hide();
            }
        });
        stage.setOnCloseRequest(event -> event.consume());

        URL iconURL = ResourceLoader.getInstance().getResourceURL("icon.png");
        stage.getIcons().add(new javafx.scene.image.Image(iconURL.openStream()));

        loadComponents(eventBus);

        // connect the client
        try {
            client.connect();
        } catch (Exception e) {
            Alerts.exception(e);
            System.exit(1);
        }

        eventBus.post(ShowComponentEvent.of(LoginView.class));
        new HotkeyListener(KEYSTROKE_HOTKEY, eventBus);
    }

    private void loadComponents(EventBus eventBus) {
        new LoginView(eventBus);
        new MainView(eventBus);
        new ChangePasswordView(eventBus);
    }

    @Subscribe
    private void onSendToTray(SendToTrayEvent event) {
        Platform.runLater(() -> {
            stage.setIconified(true);
        });
    }

    @Subscribe
    private void onShowStage(ShowStageEvent event) {
        Platform.runLater(() -> {
            stage.show();
        });
    }

    @Subscribe
    private void onSendingAlarm(ClientAlarmMessage message) {
        ResourceBundle resourceBundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
        Platform.runLater(() -> {
            Notifications.create() //
                    .title(resourceBundle.getString("AlarmSendingTitle")) //
                    .text(resourceBundle.getString("AlarmSendingText")) //
                    .owner(notificationStage) //
                    .position(Pos.BOTTOM_RIGHT) //
                    .hideAfter(Duration.seconds(15)) //
                    .showInformation();
        });
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
}
