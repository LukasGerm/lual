package github.lual.messages.types;

import github.lual.messages.base.ServerMessage;
import github.lual.messages.base.ServerMessagePattern;

@ServerMessagePattern("^3\\|3$")
public class ServerChangePasswordErrorMessage extends ServerMessage {

    @Override
    public void parse(String message) {

    }
}
