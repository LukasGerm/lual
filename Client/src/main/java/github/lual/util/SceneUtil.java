package github.lual.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Util class for scenes.
 */
public final class SceneUtil {

    /**
     * Returns the scene annotation.
     *
     * @param component The component to scan for.
     * @return Scene annotation
     */
    public final static Scene getSceneAnnotation(Object component) {
        if (component == null) {
            throw new NullPointerException("The component must not be null.");
        }
        if (!component.getClass().isAnnotationPresent(github.lual.util.Scene.class)) {
            throw new IllegalArgumentException("The object needs to have the annotation github.lual.util.Scene on the type.");
        }
        return component.getClass().getAnnotationsByType(github.lual.util.Scene.class)[0];
    }

    /**
     * Loads the fxml file from resources.
     *
     * @param fxml the path to fxml file.
     * @return Loaded fxml as Pane
     * @throws IOException
     */
    public final static Pane loadFXML(String fxml) throws IOException {
        return FXMLLoader.load(ResourceLoader.getInstance().getResourceURL(fxml));
    }
}
