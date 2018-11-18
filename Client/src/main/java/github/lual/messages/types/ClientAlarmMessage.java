package github.lual.messages.types;

import github.lual.messages.base.ClientMessage;
import github.lual.messages.base.ClientMessageFormat;

@ClientMessageFormat("4|%s")
public class ClientAlarmMessage extends ClientMessage {

    private final String token;

    public ClientAlarmMessage(String token) {
        this.token = token;
    }

    @Override
    protected String[] getValues() {
        return new String[] { token };
    }
}
