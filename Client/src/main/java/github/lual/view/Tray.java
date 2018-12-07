package github.lual.view;

import com.google.common.eventbus.EventBus;
import github.lual.Configuration;
import github.lual.util.ResourceLoader;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static github.lual.Main.WINDOW_HEIGHT;
import static github.lual.Main.WINDOW_WIDTH;

public class Tray {

    public Tray(Stage stage, EventBus eventBus) {
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
        MenuItem logoutMenuItem = new MenuItem(bundle.getString("TrayMenuLogoutApp"));
        popupMenu.add(openAppMenuItem);
        popupMenu.add(logoutMenuItem);
        popupMenu.add(closeAppMenuItem);

        openAppMenuItem.addActionListener(event -> {
            Platform.runLater(() -> {
                stage.show();
                stage.setWidth(WINDOW_WIDTH);
                stage.setHeight(WINDOW_HEIGHT);
                stage.setIconified(false);
                stage.toFront();
            });
        });
        closeAppMenuItem.addActionListener(event -> {
            Platform.runLater(() -> {
                System.exit(0);
            });
        });
        logoutMenuItem.addActionListener(event -> {
            try {
                Configuration.getInstance().setJWT(null);
                Configuration.getInstance().save();
            } catch (IOException e) {

            }
            eventBus.post(ShowComponentEvent.of(LoginView.class));
            Platform.runLater(() -> {
                stage.show();
                stage.setWidth(WINDOW_WIDTH);
                stage.setHeight(WINDOW_HEIGHT);
                stage.setIconified(false);
                stage.toFront();
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
