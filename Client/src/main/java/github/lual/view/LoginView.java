package github.lual.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.hash.Hashing;
import github.lual.Configuration;
import github.lual.messages.types.*;
import github.lual.util.Scene;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Scene("login.fxml")
public class LoginView extends BaseView {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    public LoginView(EventBus eventBus) {
        super(eventBus);
    }

    @FXML
    public void initialize() {
        btnLogin.setOnAction(event -> onLoginButtonClicked());
        if (Configuration.getInstance().getJWT() != null) {
            String jwt = Configuration.getInstance().getJWT();
            getEventBus().post(new ClientTokenLoginMessage());
        }
    }

    private void onLoginButtonClicked() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        if ("".equals(username) || "".equals(password)) {
            return;
        }
        ClientLoginMessage loginMessage = new ClientLoginMessage(username, Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString());
        getEventBus().post(loginMessage);
    }

    private void onError(String error) {
        Platform.runLater(() -> {
            Alerts.error("LoginErrorDialogTitle", error, true);
        });
    }

    @Subscribe
    private void onServerLoginInvalidData(ServerLoginInvalidDataMessage message) {
        onError("LoginErrorDialogDataWrong");
    }

    @Subscribe
    private void onServerLoginPasswordChangeRequired(ServerLoginPasswordChangeRequiredMessage message) {
        onError("LoginErrorDialogPasswordChangeRequired");
    }

    @Subscribe
    private void onServerLoginUserNotFound(ServerLoginUserNotFoundMessage message) {
        onError("LoginErrorDialogUserNotFound");
    }

    @Subscribe
    private void onServerLoginOk(ServerLoginOkMessage message) throws IOException {
        // save the token.
        Configuration.getInstance().setJWT(message.getToken());
        Configuration.getInstance().save();
        ClientTokenLoginMessage loginMessage = new ClientTokenLoginMessage();
        getEventBus().post(loginMessage);
    }

    @Subscribe
    private void onServerTokenLoginRequired(ServerTokenLoginRequiredMessage message) {
        onError("LoginErrorDialogTokenLoginFailed");
    }

    @Subscribe
    private void onServerTokenLoginOk(ServerTokenLoginOkMessage message) {
        // FIXME: Only for testing
        Platform.runLater(() -> {
            Alerts.info("Login result", "Login using token " + Configuration.getInstance().getJWT() + " successfull", false);
        });
    }
}
