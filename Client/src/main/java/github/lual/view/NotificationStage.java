package github.lual.view;

import github.lual.util.ResourceLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

/**
 * Custom implementation of a stage for the Notifications of ControlsFX.
 */
public class NotificationStage extends Stage {

    private static final String CSS_FILE = "notificationpopup.css";
    private static final double DEFAULT_HEIGHT = 400;
    private static final double DEFAULT_WIDTH = 600;
    private static final double DEFAULT_BOTTOM_MARGIN = 50;

    private final double bottomMargin;

    public NotificationStage() {
        this(DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_BOTTOM_MARGIN);
    }

    public NotificationStage(double height, double width, double bottomMargin) {
        super();
        this.bottomMargin = bottomMargin;
        URL css = ResourceLoader.getInstance().getResourceURL(CSS_FILE);
        Scene scene = new Scene(new FlowPane());
        scene.getStylesheets().add(css.toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        initStyle(StageStyle.UTILITY);
        setScene(scene);
        setMinHeight(height);
        setMinWidth(width);
        setMaxHeight(height);
        setMaxWidth(width);
        setOnCloseRequest(event -> event.consume());
    }

    public void showInBackground() {
        show();
        setX(Screen.getPrimary().getBounds().getMaxX() - getWidth());
        setY(Screen.getPrimary().getBounds().getMaxY() - getHeight() - bottomMargin);
        toBack();
    }
}
