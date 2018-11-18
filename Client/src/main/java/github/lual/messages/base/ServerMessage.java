package github.lual.messages.base;

public abstract class ServerMessage {

    public boolean matches(String message) {
        if (message == null) {
            return false;
        }
        ServerMessagePattern[] patterns = this.getClass().getAnnotationsByType(ServerMessagePattern.class);
        if (patterns == null || patterns.length == 0) {
            return false;
        }
        ServerMessagePattern pattern = patterns[0];
        return message.matches(pattern.value());
    }

    public abstract void parse(String message);
}
