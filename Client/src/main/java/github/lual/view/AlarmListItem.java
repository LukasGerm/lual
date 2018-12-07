package github.lual.view;

import github.lual.Configuration;
import github.lual.messages.types.ServerAlarmMessage;
import github.lual.util.ResourceLoader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AlarmListItem {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final LocalDateTime time;
    private final ServerAlarmMessage message;

    public AlarmListItem(ServerAlarmMessage message) {
        this.time = LocalDateTime.now();
        this.message = message;
    }

    @Override
    public String toString() {
        ResourceBundle bundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
        String alarmMessage = String.format(bundle.getString("AlarmText"), message.getFirstName(), message.getLastName(), message.getRoom());
        return String.format("%s - %s", time.format(FORMATTER), alarmMessage);
    }
}
