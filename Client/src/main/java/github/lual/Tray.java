package github.lual;

import github.lual.util.ResourceLoader;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Tray {

    public Tray(Stage stage) {
        if (!SystemTray.isSupported()) {
            return;
        }
        ResourceBundle bundle = ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage());
        URL iconResource = ResourceLoader.getInstance().getResourceURL("icon.png");
        Image iconImage;
        try {
            iconImage = ImageIO.read(iconResource);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // load Image
        PopupMenu popupMenu = new PopupMenu();
        MenuItem openAppMenuItem = new MenuItem(bundle.getString("TrayMenuOpenApp"));
        MenuItem closeAppMenuItem = new MenuItem(bundle.getString("TrayMenuCloseApp"));
        popupMenu.add(openAppMenuItem);
        popupMenu.add(closeAppMenuItem);

        openAppMenuItem.addActionListener(event -> {
            Platform.runLater(() -> {
                stage.setIconified(false);
                stage.toFront();
            });
        });
        closeAppMenuItem.addActionListener(event -> {
            Platform.runLater(() -> {
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            });
        });

        TrayIcon trayIcon = new TrayIcon(iconImage, bundle.getString("AppTitle"), popupMenu);
        trayIcon.setImageAutoSize(true);
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
