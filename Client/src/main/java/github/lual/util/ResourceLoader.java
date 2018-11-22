package github.lual.util;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Util class for loading resources.
 */
public final class ResourceLoader {
    private static ResourceLoader instance;

    private final Map<String, URL> cache;
    private final Map<String, ResourceBundle> resourceBundleCache;

    private ResourceLoader() {
        cache = new HashMap<>();
        resourceBundleCache = new HashMap<>();
    }

    /**
     * Returns the singleton instance of ResourceLoader.
     *
     * @return Singleton instance of ResourceLoader
     */
    public synchronized final static ResourceLoader getInstance() {
        if (instance == null) {
            instance = new ResourceLoader();
        }
        return instance;
    }

    /**
     * Returns the resource as URL
     *
     * @param resource The path of the resorce
     * @throws IOException
     */
    public synchronized final URL getResourceURL(String resource) {
        if (cache.containsKey(resource)) {
            return cache.get(resource);
        }
        URL url = getClass().getClassLoader().getResource(resource);
        cache.put(resource, url);
        return url;
    }

    /**
     * Returns the resource bundle
     *
     * @param lang the language short key
     */
    public synchronized final ResourceBundle getResourceBundle(String lang) {
        if (resourceBundleCache.containsKey(lang)) {
            return resourceBundleCache.get(lang);
        }
        if (getClass().getClassLoader().getResource(String.format("lang_%s.properties", lang)) == null) {
            return null;
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle(String.format("lang_%s", lang));
        resourceBundleCache.put(lang, resourceBundle);
        return resourceBundle;
    }
}
