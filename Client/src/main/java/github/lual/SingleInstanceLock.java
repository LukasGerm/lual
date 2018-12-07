package github.lual;

import github.lual.util.ResourceLoader;

import java.io.*;

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
        long ownPid = ProcessHandle.current().pid();
        long existingPid = -1;
        if (lockFile.exists()) {
            try (FileReader fileReader = new FileReader(lockFile)) {
                try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    String line = bufferedReader.readLine();
                    if (line != null && line.matches("^[0-9]+$")) {
                        existingPid = Long.parseLong(line);
                    }
                }
            }
      }

        if (existingPid > -1 && ProcessHandle.of(existingPid).isPresent() && existingPid != ownPid) {
            throw new IllegalStateException(ResourceLoader.getInstance().getResourceBundle(Configuration.getInstance().getResourceBundleLanguage()).getString("LockfilePresent"));
        }

        try (FileWriter fileWriter = new FileWriter(lockFile)) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(String.valueOf(ownPid));
            }
        }
        lockFile.deleteOnExit();
    }

}
