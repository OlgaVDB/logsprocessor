import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class OldLogsAggregator {
    public static final String LOGS_PATH = "C:/Users/oiv/Desktop/Sibelga LOGS";
    public static final String DIR_PATH = LOGS_PATH + "/20170704 1100";
    public static final String FILE_PATH = DIR_PATH + "/201707041100.txt";

    public static void main(String[] args) throws IOException {
        createFile(FILE_PATH);

        Files.list(Paths.get(DIR_PATH))
                .filter(file -> file.getFileName().toString().contains(".log."))
                .sorted((f2, f1) -> getOrdinal(f1).compareTo(getOrdinal(f2)))
                .forEach(file -> {
                    System.out.println(file.getFileName().toString());
                    try {
                        Files.write(Paths.get(FILE_PATH), (Iterable<String>)Files.lines(file)::iterator, StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static Integer getOrdinal(Path f) {
        String s = f.getFileName().toString();
        return new Integer(s.substring(s.lastIndexOf(".")+1, s.length()));
    }

    private static void createFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
    }
}
