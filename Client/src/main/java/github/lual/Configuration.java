package github.lual;

import java.io.*;
import java.util.Properties;

public class Configuration {

    private static Configuration configuration;
    private final Properties properties;
    private final File confFile;

    private Configuration() {
        properties = new Properties();
        confFile = new File("config.properties");
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

    public String getJWT() {
        String jwt = properties.getProperty("jwt");
        if (jwt == null || jwt.trim().length() < 1) {
            return null;
        }
        return jwt;
    }

    public Configuration setJWT(String jwt) {
        if (jwt == null || jwt.trim().length() < 1) {
            return this;
        }
        properties.setProperty("jwt", jwt);
        return this;
    }

    public void save() throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(confFile)) {
            properties.store(fileOutputStream, null);
        }
    }
}
