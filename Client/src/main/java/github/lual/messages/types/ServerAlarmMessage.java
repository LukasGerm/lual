package github.lual.messages.types;

import github.lual.messages.base.ServerMessage;
import github.lual.messages.base.ServerMessagePattern;

@ServerMessagePattern("^4\\|.+\\|.+\\|.+$")
public class ServerAlarmMessage extends ServerMessage {

    private String room;
    private String firstName;
    private String lastName;

    @Override
    public void parse(String message) {
        String values[] = message.split("\\|");
        this.room = values[1];
        this.firstName = values[2];
        this.lastName = values[3];
    }

    public String getRoom() {
        return room;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
