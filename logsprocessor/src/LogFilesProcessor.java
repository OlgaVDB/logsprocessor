import model.LogFile;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class LogFilesProcessor {
    private static final String DIR_PATH = "C:/Users/oiv/Desktop/Sibelga LOGS/";
    private static SortedSet<LogFile> logFileSortedSet = new TreeSet<>();

    public static SortedSet<LogFile> process() throws IOException {
        File dir = new File(DIR_PATH);

        getLogFilesSortedSet(dir);

        return logFileSortedSet;
    }

    private static void getLogFilesSortedSet(File dir) {
        final List<File> filesInDir = Arrays.asList(dir.listFiles());

        for (File file : filesInDir) {
            if (file.isDirectory()) {
                getLogFilesSortedSet(file);
            } else if (file.getName().contains("log")) {
                final LogFile logFile = new LogFile(Paths.get(file.getAbsolutePath()), new DateTime(file.lastModified()));
                logFileSortedSet.add(logFile);
            }
        }
    }


}
