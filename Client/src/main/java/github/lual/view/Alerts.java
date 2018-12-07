package github.lual.view;

import github.lual.Configuration;
import github.lual.SettingsDialogModel;
import github.lual.util.ResourceLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

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

        setIcon(alert);
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

        setIcon(alert);
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

        setIcon(alert);
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
        setIcon(alert);
        alert.showAndWait();
    }

    public static Optional<SettingsDialogModel> settingsDialog() {
        ResourceBundle bundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
        Dialog<SettingsDialogModel> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("SettingsDialogTitle"));
        dialog.setHeaderText(bundle.getString("SettingsDialogHeaderText"));

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        Node okButtonNode = dialog.getDialogPane().lookupButton(ButtonType.OK);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField host = new TextField();
        host.setPromptText(bundle.getString("SettingsDialogHostname"));

        TextField port = new TextField();
        port.setPromptText(bundle.getString("SettingsDialogPort"));

        gridPane.add(new Label(bundle.getString("SettingsDialogHostname")), 0, 0);
        gridPane.add(host, 1, 0);
        gridPane.add(new Label(bundle.getString("SettingsDialogPort")), 0, 1);
        gridPane.add(port, 1, 1);

        dialog.getDialogPane().setContent(gridPane);

        AtomicBoolean hostOk = new AtomicBoolean(false);
        AtomicBoolean portOk = new AtomicBoolean(false);

        Runnable checkRunnable = () -> {
            boolean enabled = hostOk.get() && portOk.get();
            okButtonNode.setDisable(!enabled);
        };

        host.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().length() < 1) {
                hostOk.set(false);
            } else {
                hostOk.set(true);
            }
            checkRunnable.run();
        });

        port.textProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue == null || newValue.trim().length() < 1 || !newValue.trim().matches("^[0-9]+$")) {
               portOk.set(false);
           } else {
               portOk.set(true);
           }
           checkRunnable.run();
        });

        if (Configuration.getInstance().getHost() != null) {
            host.setText(Configuration.getInstance().getHost());
        }
        if (Configuration.getInstance().getPort() != null) {
            port.setText(String.valueOf(Configuration.getInstance().getPort()));
        }

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton != ButtonType.OK) {
                return null;
            }
            SettingsDialogModel model = new SettingsDialogModel();
            model.setHost(host.getText().trim());
            model.setPort(Integer.parseInt(port.getText().trim()));
            return model;
        });

        setIcon(dialog);
        return dialog.showAndWait();
    }

    private static void setIcon(Dialog alert) {
        Window window = alert.getDialogPane().getScene().getWindow();
        if (!(window instanceof Stage)) {
            return;
        }
        Stage stage = (Stage) window;
        stage.getIcons().add(new Image(ResourceLoader.getInstance().getResourceURL("icon.png").toExternalForm()));
    }
}
