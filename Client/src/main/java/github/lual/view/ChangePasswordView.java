package github.lual.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.hash.Hashing;
import github.lual.messages.types.ClientChangePasswordMessage;
import github.lual.messages.types.ServerChangePasswordErrorMessage;
import github.lual.messages.types.ServerChangePasswordOkMessage;
import github.lual.util.Scene;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.nio.charset.StandardCharsets;

@Scene("changePassword.fxml")
public class ChangePasswordView extends BaseView {

    private String eventUsername = "";

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
        this.txtUsername.setEditable(false);
        this.txtUsername.setDisable(true);
        this.txtUsername.setText(eventUsername);
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
        getEventBus().post(new ClientChangePasswordMessage(username, Hashing.sha512().hashString(newPassword, StandardCharsets.UTF_8).toString()));
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

    @Subscribe
    private void onViewChangeUsername(ShowComponentEvent event) {
        if (event.getComponentClass() == null || !event.getComponentClass().isAssignableFrom(ChangePasswordView.class)) {
            return;
        }
        if (event.getData() != null && event.getData() instanceof String) {
            this.eventUsername = (String) event.getData();
            if (this.txtUsername != null) {
                this.txtUsername.setText(eventUsername);
            }
        }
    }
}
