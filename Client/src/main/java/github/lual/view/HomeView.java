package github.lual.view;

import com.google.common.eventbus.EventBus;
import github.lual.util.Scene;

@Scene("home.fxml")
public class HomeView extends BaseView {

    public HomeView(EventBus eventBus) {
        super(eventBus);
    }
}
