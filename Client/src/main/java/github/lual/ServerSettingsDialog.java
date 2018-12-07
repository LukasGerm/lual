package github.lual;

import github.lual.view.Alerts;

import java.io.IOException;
import java.util.Optional;

public class ServerSettingsDialog {

    public static boolean showDialog() throws IOException {
        Optional<SettingsDialogModel> model = Alerts.settingsDialog();
        if (!model.isPresent()) {
            return false;
        }
        SettingsDialogModel settingsModel = model.get();
        Configuration.getInstance().setHost(settingsModel.getHost()).setPort(settingsModel.getPort()).save();
        return true;
    }
}
