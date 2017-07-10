import model.LogFile;
import model.RunInstance;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

public class RunsCreator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
    private static SortedMap<DateTime, RunInstance> runsSortedSets = new TreeMap<>();

    public static SortedMap<DateTime, RunInstance> process(SortedSet<LogFile> logFileSortedSet) throws IOException {
        firstPass(logFileSortedSet);

        for (RunInstance run : runsSortedSets.values()) {

            for (LogFile logFile : logFileSortedSet.subSet(run.getFilenames().last(), logFileSortedSet.last())) {
                if (run.hasReachedEnd()) {
                    break;
                }
                String filename = logFile.getFullPath().toString();
//                System.out.println(filename + ": " + logFile.getModDate() + " - " + String.valueOf(Files.lines(logFile.getFullPath()).count()));
                File file = new File(filename);
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line = reader.readLine();
                DateTime currentDateTime = parseLineHourMinute(line);


                while (line != null && !line.isEmpty() && !run.isStarted() && !line.contains(Constants.START_ANALYSIS)) {
                    currentDateTime = parseLineHourMinute(line);
                    line = reader.readLine();
                }

                DateTime previousDateTime = currentDateTime;

                while (line != null) {
                    if (!line.contains(Constants.SLEEPING)) {
                        currentDateTime = parseLineDate(line);
//                        if (currentDateTime.getHourOfDay() == 3 && currentDateTime.getMinuteOfHour() == 24 && currentDateTime.getSecondOfMinute() == 40) {
//                            System.out.println("STOP");
//                        }
//                        if (currentDateTime.getHourOfDay() == 3 && currentDateTime.getMinuteOfHour() == 43 && currentDateTime.getSecondOfMinute() == 42) {
//                            System.out.println("STOP");
//                        }
//                        if (currentDateTime.getHourOfDay() == 3 && currentDateTime.getMinuteOfHour() == 44 && currentDateTime.getSecondOfMinute() == 10) {
//                            System.out.println("STOP");
//                        }

                        if (currentDateTime.isBefore(previousDateTime.plus(360000))) {
                            if (line.contains(Constants.START_ANALYSIS)) {
                                run.setStarted(true);
                            }
                            if (line.contains(Constants.FINISHED_ANALYSIS)) {
                                run.setFinishedAnalysis(true);
                            }
                            if (line.contains(Constants.FINISHED_DEFAULT_ACTION)) {
                                run.setFinishedAction(true);
                            }

                            if (!line.contains(Constants.SLEEPING)) {
                                run.addLine(line);
                            }
                            previousDateTime = currentDateTime;

                        } else {
                            run.setReachedEnd(true);
                            break;
                        }
                    }
                    line = reader.readLine();
                }
            }

            System.out.println(run.getFilenames().last().toString() + " - " + run.getContent().size());
        }
//        for (LogFile logFile : logFileSortedSet) {
//            String filename = logFile.getFullPath().toString();
//            System.out.println(filename + ": " + logFile.getModDate() + " - " + String.valueOf(Files.lines(logFile.getFullPath()).count()));
//            File file = new File(filename);
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            String line = reader.readLine();
//
//            while (line != null && !line.isEmpty()) {
//                DateTime lineHour = parseLineHour(line);
//                if (line.contains(Constants.START_ANALYSIS) || newRun(currentHour, lineHour)) {
//                    currentHour = lineHour;
//                    currentRun = new RunInstance();
//                    currentRun.setStarted(line.contains(Constants.START_ANALYSIS));
//                    runsSortedSets.put(lineHour, currentRun);
//                }
//
//                if (line.contains(Constants.FINISHED_DEFAULT_ACTION)) {
//                    currentRun.setFinishedAction(true);
//                }
//
//                if (line.contains(Constants.FINISHED_ANALYSIS)) {
//                    currentRun.setFinishedAnalysis(true);
//                }
//
//                if (!line.contains(Constants.SLEEPING)) {
//                    currentRun.addLine(line);
//                }
//                line = reader.readLine();
//            }
//        }

        for (DateTime runDateTime : runsSortedSets.keySet()) {
            final RunInstance runInstance = runsSortedSets.get(runDateTime);
//            System.out.println(runDateTime);
            for (LogFile logFile : runInstance.getFilenames()) {
//                System.out.println("   " + logFile.toString());
            }
        }

        return runsSortedSets;
    }

    private static void firstPass(SortedSet<LogFile> logFileSortedSet) throws IOException {
        for (LogFile logFile : logFileSortedSet) {
            String filename = logFile.getFullPath().toString();

//            System.out.println(filename + ": " + logFile.getModDate() + " - " + String.valueOf(Files.lines(logFile.getFullPath()).count()));

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();

            while (line != null && !line.isEmpty()) {
                if (line.contains(Constants.START_ANALYSIS)) {
                    DateTime lineHourMinute = parseLineHourMinute(line);

                    if (!runsSortedSets.containsKey(lineHourMinute)) {
                        runsSortedSets.put(lineHourMinute, new RunInstance(lineHourMinute, logFile));
                    } else {
                        runsSortedSets.get(lineHourMinute).addFilename(logFile);
                    }
                }
                line = reader.readLine();
            }
        }
    }

    private static boolean newRun(DateTime currentHour, DateTime newHour) {
        if (runsSortedSets.isEmpty()) {
//            System.out.println("runsSortedSets.isEmpty()");
            return true;
        }

        if (currentHour == null) {
//            System.out.println("currentHour == null");
            return true;
        }

        if (currentHour.getDayOfMonth() != newHour.getDayOfMonth()) {
//            System.out.println("currentHour.getDayOfMonth() != newHour.getDayOfMonth()");
            return true;
        }

        if (newHour.getHourOfDay() - currentHour.getHourOfDay() <= 5 && !runsSortedSets.containsKey(currentHour)) {
//            System.out.println("newHour.getHourOfDay() - currentHour.getHourOfDay() <= 5 && !runsSortedSets.containsKey(currentHour)");
            return true;
        }

        if (newHour.getHourOfDay() - currentHour.getHourOfDay() > 5) {
//            System.out.println("newHour.getHourOfDay() - currentHour.getHourOfDay() > 5");
            return true;
        }

        return false;
    }

    private static DateTime parseLineHour(String line) {
        final DateTime dateTime = parseLineDate(line);
        return new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getHourOfDay(), 0);
    }

    private static DateTime parseLineHourMinute(String line) {
        final DateTime dateTime = parseLineDate(line);
        return new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
    }

    private static DateTime parseLineDate(String line) {
        final String substring = line.substring(line.indexOf("2017"), line.indexOf("] - "));
        return DateTime.parse(substring, FORMATTER);
    }
}
