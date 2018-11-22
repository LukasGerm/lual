package github.lual;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class Configuration {

    private static Configuration configuration;
    private final Properties properties;

    private Configuration() {
        properties = new Properties();
        File confFile = new File("config.properties");
        if (!confFile.exists()) {
            return;
        }
        try (FileReader fileReader = new FileReader(confFile)) {
            properties.load(fileReader);
        } catch(Exception e) {
            // should not happen
        }
    }

    public static Configuration getInstance() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getResourceBundleLanguage() {
        String lang = properties.getProperty("language");
        if (lang == null || lang.trim().length() < 1) {
            return "de";
        }
        return lang;
    }

    public String getHost() {
        String host = properties.getProperty("host");
        if (host == null || host.trim().length() < 1) {
            return "127.0.0.1";
        }
        return host;
    }

    public int getPort() {
        String port = properties.getProperty("port");
        if (port == null || port.trim().length() < 1 || !port.matches("^[0-9]+$")) {
            return 8000;
        }
        return Integer.parseInt(port);
    }
}
