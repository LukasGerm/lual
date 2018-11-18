package github.lual.messages.types;

import github.lual.messages.base.ClientMessage;
import github.lual.messages.base.ClientMessageFormat;

@ClientMessageFormat("3|%s|%s")
public class ClientChangePasswordMessage extends ClientMessage {

    private final String username;
    private final String newPassword;

    public ClientChangePasswordMessage(String username, String newPassword) {
        this.username = username;
        this.newPassword = newPassword;
    }

    @Override
    protected String[] getValues() {
        return new String[] { username, newPassword };
    }
}
