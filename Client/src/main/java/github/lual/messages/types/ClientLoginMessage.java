package github.lual.messages.types;

import github.lual.messages.base.ClientMessage;
import github.lual.messages.base.ClientMessageFormat;

@ClientMessageFormat("1|%s|%s")
public class ClientLoginMessage extends ClientMessage {

    private final String username;
    private final String password;

    public ClientLoginMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected String[] getValues() {
        return new String[] { username, password };
    }
}
