package github.lual.util;

import github.lual.Configuration;
import github.lual.view.BaseView;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
     * @param controller the fxml controller
     * @return Loaded fxml as Pane
     * @throws IOException
     */
    public final static Pane loadFXML(String fxml, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(controller);
        fxmlLoader.setResources(ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage()));
        fxmlLoader.setLocation(ResourceLoader.getInstance().getResourceURL(fxml));
        return fxmlLoader.load();
    }
}
