package github.lual.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import github.lual.messages.types.ClientChangePasswordMessage;
import github.lual.messages.types.ServerChangePasswordErrorMessage;
import github.lual.messages.types.ServerChangePasswordOkMessage;
import github.lual.util.Scene;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Scene("changePassword.fxml")
public class ChangePasswordView extends BaseView {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnChangePassword;

    public ChangePasswordView(EventBus eventBus) {
        super(eventBus);
    }

    @FXML
    public void initialize() {
        this.btnChangePassword.setOnAction(event -> onPasswordChange());
    }

    private void onPasswordChange() {
        String username = txtUsername.getText();
        String newPassword = txtPassword.getText();
        if (username == null || username.trim().length() < 1) {
            return;
        }
        if (newPassword == null || newPassword.trim().length() < 1) {
            return;
        }
        getEventBus().post(new ClientChangePasswordMessage(username, newPassword));
    }

    @Subscribe
    private void onPasswordChangeOk(ServerChangePasswordOkMessage message) {
        Platform.runLater(() -> {
            Alerts.info("ChangePasswordDialogTitle", "ChangePasswordDialogOkText", true);
        });
        getEventBus().post(ShowComponentEvent.of(LoginView.class));
    }

    @Subscribe
    private void onPasswordChangeError(ServerChangePasswordErrorMessage message) {
        Platform.runLater(() -> {
            Alerts.error("ChangePasswordDialogTitle", "ChangePasswordDialogErrorText", true);
        });
    }
}
