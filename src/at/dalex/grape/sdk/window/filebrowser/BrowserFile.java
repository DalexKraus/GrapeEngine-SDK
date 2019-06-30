package at.dalex.grape.sdk.window.filebrowser;

import java.io.File;
import java.net.URI;

/**
 * This class contains information for the
 * {@link FileBrowserItem} about the path
 */
public class BrowserFile extends File {

    private String rootName;

    public BrowserFile(String pathname) {
        super(pathname);
    }

    public BrowserFile(String rootName, File file) {
        this(file.getPath());
        this.rootName = rootName;
    }

    public BrowserFile(String parent, String child) {
        super(parent, child);
    }

    public BrowserFile(File parent, String child) {
        super(parent, child);
    }

    public BrowserFile(URI uri) {
        super(uri);
    }

    @Override
    public String toString() {
        return rootName != null ? rootName : getName();
    }
}
