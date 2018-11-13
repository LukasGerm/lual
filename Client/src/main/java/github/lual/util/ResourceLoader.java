package github.lual.util;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class for loading resources.
 */
public final class ResourceLoader {
    private static ResourceLoader instance;

    private final Map<String, URL> cache;

    private ResourceLoader() {
        cache = new HashMap<>();
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
}
