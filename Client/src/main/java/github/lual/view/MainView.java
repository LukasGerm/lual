package github.lual.view;

import com.google.common.eventbus.EventBus;
import github.lual.messages.types.ClientAlarmMessage;
import github.lual.util.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
}
