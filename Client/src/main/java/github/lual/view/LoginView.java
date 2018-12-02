package github.lual.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.hash.Hashing;
import github.lual.Configuration;
import github.lual.messages.types.*;
import github.lual.util.Scene;
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
    }

    private void onLoginButtonClicked() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        if ("".equals(username) || "".equals(password)) {
            System.err.println("Username/password not filled");
            return;
        }
        ClientLoginMessage loginMessage = new ClientLoginMessage(username, Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString());
        getEventBus().post(loginMessage);
        System.out.println("Put login message to event bus");
    }

    private void onError(String error) {
        System.err.println(error);
    }

    @Subscribe
    private void onServerLoginInvalidData(ServerLoginInvalidDataMessage message) {
        onError("invalid data for login");
    }

    @Subscribe
    private void onServerLoginPasswordChangeRequired(ServerLoginPasswordChangeRequiredMessage message) {
        onError("password must be changed for login");
    }

    @Subscribe
    private void onServerLoginOk(ServerLoginOkMessage message) throws IOException {
        Configuration.getInstance().setJWT(message.getToken());
        Configuration.getInstance().save();
        ClientTokenLoginMessage loginMessage = new ClientTokenLoginMessage();
        getEventBus().post(loginMessage);
    }

    @Subscribe
    private void onServerTokenLoginRequired(ServerTokenLoginRequiredMessage message) {
        onError("token login failed");
    }

    @Subscribe
    private void onServerTokenLoginOk(ServerTokenLoginOkMessage message) {
        System.out.println("login ok");
    }
}
