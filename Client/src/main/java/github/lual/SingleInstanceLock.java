package github.lual;

import github.lual.util.ResourceLoader;

import java.io.File;
import java.io.IOException;

public class SingleInstanceLock {

    private static final File lockFile = new File("lual.lock");

    private static SingleInstanceLock instance;

    private SingleInstanceLock() {
    }

    public static synchronized SingleInstanceLock getInstance() {
        if (instance == null) {
            instance = new SingleInstanceLock();
        }
        return instance;
    }

    public synchronized void lock() throws IOException {
        if (lockFile.exists()) {
            throw new IllegalStateException(ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage()).getString("LockfilePresent"));
        }
        lockFile.createNewFile();
        lockFile.deleteOnExit();
    }

}
