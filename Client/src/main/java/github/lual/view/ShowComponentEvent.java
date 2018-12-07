package github.lual.view;

public class ShowComponentEvent<T extends BaseView> {

    private final Class<T> componentClass;
    private final T component;
    private Object data;

    private ShowComponentEvent(Class<T> componentClass, T component) {
        this.componentClass = componentClass;
        this.component = component;
    }

    public static <T extends BaseView> ShowComponentEvent<T> of(Class<T> componentClass) {
        return new ShowComponentEvent<>(componentClass, null);
    }

    public static <T extends BaseView> ShowComponentEvent<T> of(T component) {
        return new ShowComponentEvent<>(null, component);
    }

    public Class<T> getComponentClass() {
        return componentClass;
    }

    public T getComponent() {
        return component;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
