package github.lual;

import com.google.common.eventbus.EventBus;
import github.lual.net.TlsClient;
import github.lual.util.ComponentManager;
import github.lual.view.LoginView;
import github.lual.view.ShowComponentEvent;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
    private static final String WINDOW_TITLE = "Lual";

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
        final EventBus eventBus = new EventBus();
        final ComponentManager componentManager = new ComponentManager(stage, eventBus);
        final TlsClient client = new TlsClient(config.getHost(), config.getPort());
        final EventBusMessageGateway messageGateway = new EventBusMessageGateway(eventBus, client);

        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setTitle(WINDOW_TITLE);
        loadComponents(eventBus);

        // show the window
        stage.show();
        eventBus.post(ShowComponentEvent.of(LoginView.class));

        // connect the client
        client.connect();

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
    }
}
