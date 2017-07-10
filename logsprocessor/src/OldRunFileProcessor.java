import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OldRunFileProcessor {
    /* variables */
//    public static final String filename = "c://logs//27061458/27061458.txt";
    public static final String filename = OldLogsAggregator.FILE_PATH;
    public static final String when = "2017-04-07 11:00";
    public static final String INDEX_CUTOFF = "2017-07-04 ";

    /* finals */
    public static final Path path = Paths.get(filename);

    public static final String START_ANALYSIS = "Starting analysis for task 'SAP MDUS Time Series Item Exporter Electricity'";
    public static final String GET_EXPORT_OCC_START = "#getExportOccurrences() start: ";
    public static final String GET_EXPORT_CAL_START = "EdiSendOnceExportStrategy#getExportCalendars() start";
    public static final String INIT_CALENDAR_START = "(com.energyict.mdw.export.ExportCalendar:initCalendar) - ExportCalendar#initCalendar - start";
    public static final String INIT_CALENDAR_END = "(com.energyict.mdw.export.ExportCalendar:initCalendar) - ExportCalendar#initCalendar -   end";
    public static final String GET_EXPORT_CAL_END = "EdiSendOnceExportStrategy#getExportCalendars()   end";
    public static final String GET_EXPORT_OCC_CAL_START = "DefaultEdiExportStrategy#getExportOccurrences(ExportCalendar) start";
    public static final String GET_VALID_ITEMS_START = "AbstractExportStrategy#getValidItems() start";
    public static final String GET_VALID_ITEMS_END = "AbstractExportStrategy#getValidItems() end";
    public static final String GET_EXPORT_OCC_CAL_END = "DefaultEdiExportStrategy#getExportOccurrences(ExportCalendar) end";
    public static final String GET_EXPORT_OCC_END = "#getExportOccurrences() end: ";
    public static final String FINISHED_ANALYSIS = "Finished analysis for task 'SAP MDUS Time Series Item Exporter Electricity'";
    public static final String FINISHED_DEFAULT_ACTION = "Finished default action";

    public static final String INIT_CALENDAR_ITEM = "(com.energyict.mdw.export.ExportCalendar:initCalendar) - ExportCalendar#initCalendar - name";
    public static final String GET_VALID_ITEM = "AbstractExportStrategy#getValidItems() item";

    public static final String CUS_DATA_GATHERING = "Starting data gathering";
    public static final String CUS_EXPORT_OCC = "SibelgaMessageCollector:logIssues) - Export Occurrence ";
    public static final String CUS_LOG_SENDING_OF = "AbstractTimeSeriesItemExporter:logSendingOf";

    public static final String CUS_COMPLETED_SERVICE_REQUEST = "Completed Service request";

    public static final String SLEEPING = "sleeping for ";




    public static void main(String[] args) {
        runAnalysis();
        customExport();
//
//        count(CUS_LOG_SENDING_OF);

        listOccurrences(FINISHED_DEFAULT_ACTION);
    }

    private static void customExport() {
        System.out.print("      - Custom export took: ");
        printDiffBetween(getTime(FINISHED_ANALYSIS), getLastTime(FINISHED_DEFAULT_ACTION));

//        System.out.print("    - Data gathering: ");
//        printDiffBetween(getTime(CUS_DATA_GATHERING), getFirstTime(CUS_EXPORT_OCC));
//
//        System.out.print("    - Export: ");
//        printDiffBetween(getTime(CUS_EXPORT_OCC), getLastTime(CUS_COMPLETED_SERVICE_REQUEST));
    }

    private static void runAnalysis() {
        System.out.println("Run details of " + when);
        System.out.print("   Total duration: ");
        printDiffBetween(getTime(START_ANALYSIS), getLastTime(CUS_COMPLETED_SERVICE_REQUEST));

        System.out.print("      - Analysis took: ");
        printDiffBetween(getTime(START_ANALYSIS), getTime(FINISHED_ANALYSIS));

        System.out.print("         - Export calendars calculation took: ");
        printDiffBetween(getTime(GET_EXPORT_CAL_START), getTime(GET_EXPORT_CAL_END));

//        System.out.print("    - Regular: ");
//        printDiffBetween(getFirstTime(INIT_CALENDAR_START), getFirstTime(INIT_CALENDAR_END));
//
//        System.out.print("    - Updates: ");
//        printDiffBetween(getLastTime(INIT_CALENDAR_START), getLastTime(INIT_CALENDAR_END));

        System.out.print("         - Export occurrences creation took: ");
        printDiffBetween(getFirstTime(GET_EXPORT_OCC_CAL_START), getLastTime(GET_EXPORT_OCC_CAL_END));

//        System.out.print("    - Regular: ");
//        printDiffBetween(getFirstTime(GET_EXPORT_OCC_CAL_START), getFirstTime(GET_EXPORT_OCC_CAL_END));
//
//        System.out.print("    - Updates: ");
//        printDiffBetween(getLastTime(GET_EXPORT_OCC_CAL_START), getLastTime(GET_EXPORT_OCC_CAL_END));
    }

    private static void printDiffBetween(DateTime exp1, DateTime exp2) {
        final Period period = new Period(exp1, exp2);
        System.out.println(period.getHours() + ":" + period.getMinutes() + ":" + period.getSeconds() + "," + period.getMillis());
    }

    private static void listTimes(String exp) {
        System.out.println(exp);

        try (Stream<String> stream = Files.lines(path)) {
            stream
                    .filter(line -> line.contains(exp))
                    .map(line -> line.substring(line.indexOf(INDEX_CUTOFF)+11, line.indexOf("] - (")).replace(",", "\t"))
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DateTime getTime(String exp) {
//        System.out.println(exp);

        try {
            final String dateString = Files.lines(path)
                    .filter(line -> line.contains(exp))
                    .findFirst()
                    .map(line -> line.substring(line.indexOf(INDEX_CUTOFF), line.indexOf("] - (")))
                    .get();

//            System.out.println(dateString);

            return DateTime.parse(dateString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DateTime getFirstTime(String exp) {
        return getTime(exp);
    }

    private static DateTime getLastTime(String exp) {
//        System.out.println(exp);

        try {
            final List<String> dateStrings = Files.lines(path)
                    .filter(line -> line.contains(exp))
                    .map(line -> line.substring(line.indexOf(INDEX_CUTOFF), line.indexOf("] - (")))
                    .collect(Collectors.toList());

            final String dateString = dateStrings.get(dateStrings.size() - 1);

//            System.out.println(dateString);

            return DateTime.parse(dateString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void count(String exp) {
        try (Stream<String> stream = Files.lines(path)) {
            System.out.println(stream
                    .filter(line -> line.contains(exp))
                    .count() + " " + exp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listOccurrences(String exp) {
        try (Stream<String> stream = Files.lines(path)) {
            stream
                    .filter(line -> line.contains(exp))
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
