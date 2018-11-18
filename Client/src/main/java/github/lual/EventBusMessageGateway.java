package github.lual;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import github.lual.messages.base.ClientMessage;
import github.lual.messages.base.ServerMessage;
import github.lual.messages.types.*;
import github.lual.net.TlsClient;

import java.util.*;

public class EventBusMessageGateway {

    private final EventBus eventBus;
    private final TlsClient tlsClient;
    private final List<Class> serverMessageClasses;

    public EventBusMessageGateway(EventBus eventBus, TlsClient tlsClient) {
        this.eventBus = eventBus;
        this.tlsClient = tlsClient;
        this.serverMessageClasses = new ArrayList<>();
        registerServerMessageClasses();
        tlsClient.addReceiver(((message, throwable) -> handleServerMessage(message, throwable)));
        eventBus.register(this);
    }

    @Subscribe
    public void sendClientMessage(ClientMessage clientMessage) throws InterruptedException {
        this.tlsClient.sendMessage(clientMessage.getMessage());
    }

    private void handleServerMessage(String message, Throwable throwable) {
        System.out.println("Got server message: " + message); // TODO: remove
        if (throwable != null) {
            System.err.println("EventBusMessageGateway#handleServerMessage: Exception caught. See stacktrace below.");
            throwable.printStackTrace();
            return;
        }

        // find out the class and object of the message, that should be posted to eventbus
        Optional<ServerMessage> serverMessage = serverMessageClasses.stream() //
            .filter(Objects::nonNull) //
            .filter(clazz -> ServerMessage.class.isAssignableFrom(clazz)) //
            .map(this::newClassInstance) //
            .filter(Objects::nonNull) //
            .map(obj -> (ServerMessage) obj) //
            .filter(obj -> obj.matches(message)) //
            .findFirst();

        // call the parse method and post the message to the eventbus
        if (serverMessage.isPresent()) {
            serverMessage.get().parse(message);
            System.out.println(serverMessage.toString()); // TODO: remove
            eventBus.post(serverMessage.get());
        }
    }

    private Object newClassInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    private void registerServerMessageClasses() {
        Class[] classes = {
                ServerAlarmMessage.class,
                ServerChangePasswordErrorMessage.class,
                ServerChangePasswordOkMessage.class,
                ServerLoginInvalidDataMessage.class,
                ServerLoginOkMessage.class,
                ServerLoginPasswordChangeRequiredMessage.class,
                ServerTokenLoginOkMessage.class,
                ServerTokenLoginRequiredMessage.class
        };
        this.serverMessageClasses.addAll(Arrays.asList(classes));
    }
}
