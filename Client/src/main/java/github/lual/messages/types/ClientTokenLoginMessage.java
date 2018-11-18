package github.lual.messages.types;

import github.lual.messages.base.ClientMessage;
import github.lual.messages.base.ClientMessageFormat;

@ClientMessageFormat("2|%s")
public class ClientTokenLoginMessage extends ClientMessage {

    private final String token;

    public ClientTokenLoginMessage(String token) {
        this.token = token;
    }

    @Override
    protected String[] getValues() {
        return new String[] { token };
    }
}
