package github.lual.messages.base;

import github.lual.messages.base.ClientMessageFormat;

public abstract class ClientMessage {

    protected abstract String[] getValues();

    public String getMessage() {
        ClientMessageFormat[] annotations = this.getClass().getAnnotationsByType(ClientMessageFormat.class);
        if (annotations == null || annotations.length == 0) {
            throw new IllegalStateException("No annotation found on type " + this.getClass().getName());
        }
        ClientMessageFormat format = annotations[0];
        if (format.value() == null) {
            throw new IllegalStateException("Value for ClientMessageFormat is empty");
        }
        String[] values = getValues();
        if (values == null || values.length == 0) {
            return format.value();
        }
        return String.format(format.value(), values);
    }
}
