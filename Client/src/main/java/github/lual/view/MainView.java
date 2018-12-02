package github.lual.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import github.lual.Configuration;
import github.lual.messages.types.ClientAlarmMessage;
import github.lual.messages.types.ServerAlarmMessage;
import github.lual.util.ResourceLoader;
import github.lual.util.Scene;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ResourceBundle;

@Scene("main.fxml")
public class MainView extends BaseView {

    @FXML
    private Button btnAlarm;

    public MainView(EventBus eventBus) {
        super(eventBus);
    }

    @FXML
    public void initialize() {
        btnAlarm.setOnAction(event -> {
            getEventBus().post(new ClientAlarmMessage());
        });
    }

    @Subscribe
    private void onAlarm(ServerAlarmMessage message) {
        ResourceBundle resourceBundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
        Platform.runLater(() -> {
            Alerts.info(resourceBundle.getString("AlarmTitle"), String.format(resourceBundle.getString("AlarmText"), message.getFirstName(), message.getLastName(), message.getRoom()), false);
        });
    }
}
