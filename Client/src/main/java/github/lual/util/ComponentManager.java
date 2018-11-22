package github.lual.util;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import github.lual.view.ShowComponentEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Manages the components
 */
public final class ComponentManager {

    private final BorderPane parent;

    public ComponentManager(Stage primaryStage, EventBus eventBus) {
        eventBus.register(this);
        parent = new BorderPane();
        primaryStage.setScene(new Scene(parent));
    }

    /**
     * Returns the parent layout (BorderPane).
     * @return BorderPane parent layout
     */
    public BorderPane getParent() {
        return this.parent;
    }

    /**
     * Loads the component
     * @param component The component to load
     */
    public Pane loadComponent(Object component) throws IOException {
        github.lual.util.Scene scene = SceneUtil.getSceneAnnotation(component);
        return SceneUtil.loadFXML(scene.value());
    }

    /**
     * Sets the pane to the parent layout.
     * @param pane The pane to set.
     */
    public void setPane(Pane pane) {
        parent.setCenter(pane);
    }

    @Subscribe
    public void subscribeEvents(ShowComponentEvent event) throws IOException {
        if (event.getComponentClass() != null) {
            return;
        }
        setPane(loadComponent(event.getComponent()));
    }
}
