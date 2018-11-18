package github.lual.messages.types;

import github.lual.messages.base.ServerMessage;
import github.lual.messages.base.ServerMessagePattern;

@ServerMessagePattern("^1\\|1$")
public class ServerLoginInvalidDataMessage extends ServerMessage {

    @Override
    public void parse(String message) {

    }
}
