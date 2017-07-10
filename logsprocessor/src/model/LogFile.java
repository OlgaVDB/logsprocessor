package model;

import org.joda.time.DateTime;

import java.nio.file.Path;

/* Holds metadata of a log file */
public class LogFile implements Comparable{
    private Path fullPath;
    private DateTime modDate;

    public LogFile(Path fullPath, DateTime modDate) {
        this.fullPath = fullPath;
        this.modDate = modDate;
    }

    public Path getFullPath() {
        return fullPath;
    }

    public DateTime getModDate() {
        return modDate;
    }

    @Override
    public int compareTo(Object o) {
        return this.modDate.compareTo(((LogFile) o).modDate);
    }

    @Override
    public String toString() {
        return fullPath.toString() + " - " + modDate.toString();
    }
}
