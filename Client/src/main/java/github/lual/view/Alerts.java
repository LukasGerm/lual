package github.lual.view;

import github.lual.Configuration;
import github.lual.util.ResourceLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ResourceBundle;

public class Alerts {

    public static void info(String title, String message, boolean resourceBundle) {
        if (resourceBundle) {
            ResourceBundle bundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
            title = bundle.getString(title);
            message = bundle.getString(message);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void error(String title, String message, boolean resourceBundle) {
        if (resourceBundle) {
            ResourceBundle bundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
            title = bundle.getString(title);
            message = bundle.getString(message);
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void warn(String title, String message, boolean resourceBundle) {
        if (resourceBundle) {
            ResourceBundle bundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
            title = bundle.getString(title);
            message = bundle.getString(message);
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void exception(Throwable throwable) {
        ResourceBundle resourceBundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("ExceptionDialogTitle"));
        alert.setHeaderText(null);
        alert.setContentText(String.format(resourceBundle.getString("ExceptionDialogText"), throwable.getMessage()));

        String exceptionText = null;
        try (StringWriter stringWriter = new StringWriter()) {
            try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                throwable.printStackTrace(printWriter);
            }
            exceptionText = stringWriter.toString();
        } catch (IOException e) {
        }

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }
}
