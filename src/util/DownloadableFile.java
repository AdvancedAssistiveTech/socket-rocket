package util;

import java.io.File;

public class DownloadableFile {
    private final String name;
    private final String path;
    private final long size;

    public DownloadableFile(String name, String path, long size) { // for receiving serverside
        this.name = name;
        this.path = path;
        this.size = size;
    }

    public DownloadableFile(File source){ // for sending clientside
        this(source.getName(), source.getAbsolutePath(), source.length());
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "DownloadableFile{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                '}';
    }
}
