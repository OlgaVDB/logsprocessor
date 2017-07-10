import model.LogFile;
import model.RunInstance;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.SortedMap;
import java.util.SortedSet;

public class Main {

    public static void main(String[] args) {
        System.out.println("Start -----");
        try {
            SortedSet<LogFile> logFileSet = LogFilesProcessor.process();
            SortedMap<DateTime, RunInstance> runMap = RunsCreator.process(logFileSet);
            LogFilesCreator.process(runMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("----- End");
    }
}
