package github.lual;

import github.lual.util.ComponentManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
    private static final String WINDOW_TITLE = "Lual";

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final ComponentManager componentManager = new ComponentManager(stage);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setTitle(WINDOW_TITLE);

        // TODO: Show initial UI component

        stage.show();
    }
}
