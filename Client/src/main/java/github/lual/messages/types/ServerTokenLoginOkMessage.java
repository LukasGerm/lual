package github.lual.messages.types;

import github.lual.messages.base.ServerMessage;
import github.lual.messages.base.ServerMessagePattern;

@ServerMessagePattern("^2\\|ok$")
public class ServerTokenLoginOkMessage extends ServerMessage {

    @Override
    public void parse(String message) {

    }
}
