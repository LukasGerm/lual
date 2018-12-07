package github.lual.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import github.lual.SendToTrayEvent;
import github.lual.messages.types.ClientAlarmMessage;
import github.lual.messages.types.ServerAlarmMessage;
import github.lual.util.Scene;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

@Scene("main.fxml")
public class MainView extends BaseView {

    @FXML
    private ListView<AlarmListItem> alarmListView;

    public MainView(EventBus eventBus) {
        super(eventBus);
    }

    @Subscribe
    private void onAlarm(ServerAlarmMessage message) {
        Platform.runLater(() -> {
            alarmListView.getItems().add(new AlarmListItem(message));
        });
    }

    @Subscribe
    private void onMainViewOpen(ShowComponentEvent event) {
        if (event.getComponentClass() == null || !event.getComponentClass().isAssignableFrom(getClass())) {
            return;
        }
        getEventBus().post(new SendToTrayEvent());
    }
}
