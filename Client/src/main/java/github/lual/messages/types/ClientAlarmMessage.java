package github.lual.messages.types;

import github.lual.messages.base.ClientMessage;
import github.lual.messages.base.ClientMessageFormat;
import github.lual.messages.base.TokenMessage;

@ClientMessageFormat("4|%s")
public class ClientAlarmMessage extends ClientMessage implements TokenMessage {

    private String token;

    @Override
    protected String[] getValues() {
        return new String[] { token };
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
}
