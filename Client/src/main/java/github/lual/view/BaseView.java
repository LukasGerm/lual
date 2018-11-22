package github.lual.view;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public abstract class BaseView {

    private final EventBus eventBus;

    public BaseView(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    protected EventBus getEventBus() {
        return eventBus;
    }

    @Subscribe
    private void onViewChange(ShowComponentEvent showComponentEvent) {
        if (showComponentEvent.getComponent() != null) {
            return;
        }
        if (!showComponentEvent.getComponentClass().isAssignableFrom(getClass())) {
            return;
        }
        eventBus.post(ShowComponentEvent.of(this));
    }
}
