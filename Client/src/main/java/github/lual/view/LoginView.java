package github.lual.view;

import com.google.common.eventbus.EventBus;
import github.lual.Configuration;
import github.lual.messages.types.*;
import github.lual.util.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

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
        ClientLoginMessage loginMessage = new ClientLoginMessage(username, password);
        getEventBus().post(loginMessage);
        System.out.println("Put login message to event bus");
    }

    private void onError(String error) {
        System.err.println(error);
    }

    private void onServerLoginInvalidData(ServerLoginInvalidDataMessage message) {
        onError("invalid data for login");
    }

    private void onServerLoginPasswordChangeRequired(ServerLoginPasswordChangeRequiredMessage message) {
        onError("password must be changed for login");
    }

    private void onServerLoginOk(ServerLoginOkMessage message) throws IOException {
        Configuration.getInstance().setJWT(message.getToken());
        Configuration.getInstance().save();
        ClientTokenLoginMessage loginMessage = new ClientTokenLoginMessage();
        getEventBus().post(loginMessage);
    }

    private void onServerTokenLoginRequired(ServerTokenLoginRequiredMessage message) {
        onError("token login failed");
    }

    private void onServerTokenLoginOk(ServerTokenLoginOkMessage message) {
        System.out.println("login ok");
    }
}
