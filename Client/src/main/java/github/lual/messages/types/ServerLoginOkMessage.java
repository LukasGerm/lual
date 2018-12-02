package github.lual.messages.types;

import github.lual.messages.base.ServerMessage;
import github.lual.messages.base.ServerMessagePattern;

@ServerMessagePattern("^token\\|.+$")
public class ServerLoginOkMessage extends ServerMessage {

    private String token;

    @Override
    public void parse(String message) {
        String[] values = message.split("\\|");
        this.token = values[1];
    }

    public String getToken() {
        return token;
    }
}
