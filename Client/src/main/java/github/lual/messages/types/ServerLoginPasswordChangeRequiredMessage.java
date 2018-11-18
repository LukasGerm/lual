package github.lual.messages.types;

import github.lual.messages.base.ServerMessage;
import github.lual.messages.base.ServerMessagePattern;

@ServerMessagePattern("^1\\|2$")
public class ServerLoginPasswordChangeRequiredMessage extends ServerMessage {

    @Override
    public void parse(String message) {

    }
}
