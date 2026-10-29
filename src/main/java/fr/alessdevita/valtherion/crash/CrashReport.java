package fr.alessdevita.valtherion.crash;

import java.io.File;

public class CrashReport {
    private File file;
    private String title;

    public CrashReport(String title, Throwable cause) {
        this.title = title;
    }

    public static CrashReport makeCrashReport(Throwable e, String unexpectedErrorInGameLoop) {
        return new CrashReport(unexpectedErrorInGameLoop, e);
    }

    public CrashReportCategory getCategory() {
        return makeCategory("General");
    }

    public CrashReportCategory makeCategory(String categoryName) {
        return new CrashReportCategory(categoryName);
    }

    public boolean getCompleteReport() {
        return true;
    }

    public File getFile() {
        return this.file;
    }

    public boolean saveToFile(File file) {
        this.file = file;
        return file != null;
    }

    @Override
    public String toString() {
        return this.title == null ? "CrashReport" : this.title;
    }
}
