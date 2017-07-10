package model;

import org.joda.time.DateTime;

import java.util.*;

public class RunInstance {
    boolean started;
    boolean finishedAnalysis;
    boolean finishedAction;
    boolean reachedEnd = false;

    DateTime startDateTime;
    SortedSet<LogFile> logFiles = new TreeSet<>();

    Set<String> content = new LinkedHashSet<>();

    public RunInstance(DateTime lineHourMinute, LogFile filename) {
        startDateTime = lineHourMinute;
        logFiles.add(filename);
    }

    public boolean isStarted() {
        return started;
    }

    public void setReachedEnd(boolean reachedEnd) {
        this.reachedEnd = reachedEnd;
    }

    public boolean hasReachedEnd() {
        return reachedEnd;
    }

    public void addFilename(LogFile filename) {
        logFiles.add(filename);
    }

    public SortedSet<LogFile> getFilenames() {
        return logFiles;
    }

    public void setFinishedAnalysis(boolean finishedAnalysis) {
        this.finishedAnalysis = finishedAnalysis;
    }

    public void setFinishedAction(boolean finishedAction) {
        this.finishedAction = finishedAction;
    }

    public boolean isIdleRun() {
        return finishedAnalysis && !finishedAction;
    }

    public boolean isComplete() {
        return started && (finishedAnalysis || finishedAction);
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Set<String> getContent() {
        return content;
    }

    public void addLine(String line) {
        content.add(line);
    }
}
