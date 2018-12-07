package github.lual;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
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
        new Tray(stage);

        final EventBus eventBus = new EventBus();
        eventBus.register(this);
        final ComponentManager componentManager = new ComponentManager(stage, eventBus);
        final TlsClient client = new TlsClient(config.getHost(), config.getPort());
        final EventBusMessageGateway messageGateway = new EventBusMessageGateway(eventBus, client);

        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setTitle(resourceBundle.getString("AppTitle"));

        URL iconURL = ResourceLoader.getInstance().getResourceURL("icon.png");
        stage.getIcons().add(new javafx.scene.image.Image(iconURL.openStream()));

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
        new HotkeyListener(KEYSTROKE_HOTKEY, eventBus);

        // close-event required to disconnect client and cleanup resources
        stage.setOnCloseRequest(event -> {
            try {
                notificationStage.close();
                client.close();
                System.exit(0);
            } catch (IOException e) {
            }
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
}
