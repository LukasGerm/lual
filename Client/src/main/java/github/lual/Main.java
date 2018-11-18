package github.lual;

import com.google.common.eventbus.EventBus;
import github.lual.messages.types.ClientLoginMessage;
import github.lual.net.TlsClient;
import github.lual.util.ComponentManager;
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
        final ComponentManager componentManager = new ComponentManager(stage);
        final EventBus eventBus = new EventBus();
        final TlsClient client = new TlsClient("127.0.0.1", 8000);
        final EventBusMessageGateway messageGateway = new EventBusMessageGateway(eventBus, client);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setTitle(WINDOW_TITLE);

        // TODO: Show initial UI component

        // FIXME: Here only for testing purpose
        client.connect();
        messageGateway.sendClientMessage(new ClientLoginMessage("admin", "admin"));

        // show the window
        stage.show();

        // close-event required to disconnect client and cleanup resources
        stage.setOnCloseRequest(event -> {
            try {
                client.close();
            } catch (IOException e) {
            }
        });
    }
}
