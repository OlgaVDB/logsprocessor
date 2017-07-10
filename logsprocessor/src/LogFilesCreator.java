import model.RunInstance;
import org.joda.time.DateTime;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

public class LogFilesCreator {
    public static void process(SortedMap<DateTime, RunInstance> runsSortedMap) throws IOException {
        int count = runsSortedMap.size();

        for (DateTime dateTime : runsSortedMap.keySet()) {
            RunInstance run = runsSortedMap.get(dateTime);
//            if (run.isComplete()) {
                final String filename = parseFilename(run, dateTime);
                FileWriter fileWriter = new FileWriter(filename + ".txt");
                final Set<String> content = run.getContent();
                System.out.println(String.valueOf(count--) + ": " + filename + "(" + String.valueOf(content.size()) + ")");
                for (String line : content) {
                    fileWriter.write(line + "\r\n");
                }
                fileWriter.flush();
                fileWriter.close();
//            }
        }
    }

    private static String parseFilename(RunInstance run, DateTime dateTime) {
        String returner = String.valueOf(dateTime.getYear());

        String value = String.valueOf(dateTime.getMonthOfYear());
        if (value.length() == 1) value = "0" + value;
        returner += value;

        value = String.valueOf(dateTime.getDayOfMonth());
        if (value.length() == 1) value = "0" + value;
        returner += value;

        value = String.valueOf(dateTime.getHourOfDay());
        if (value.length() == 1) value = "0" + value;
        returner += value;

        value = String.valueOf(dateTime.getMinuteOfHour());
        if (value.length() == 1) value = "0" + value;
        returner += value;

        if (!run.isComplete()) {
            returner += "_incomplete";
        } else if (run.isIdleRun()) {
            returner += "_idle";
        }

        return returner;
    }
}
