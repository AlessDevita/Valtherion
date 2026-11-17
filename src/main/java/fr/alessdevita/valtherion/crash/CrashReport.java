package fr.alessdevita.valtherion.crash;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class CrashReport {
    private File file;
    private String title;
    private Throwable cause;
    private List<CrashReportCategory> categories = new ArrayList<>();
    private final long timestamp = System.currentTimeMillis();

    public CrashReport(String title, Throwable cause) {
        this.title = title;
        this.cause = cause;
    }

    public static CrashReport makeCrashReport(Throwable e, String unexpectedErrorInGameLoop) {
        return new CrashReport(unexpectedErrorInGameLoop, e);
    }

    public CrashReportCategory getCategory() {
        return makeCategory("General");
    }

    public CrashReportCategory makeCategory(String categoryName) {
        CrashReportCategory category = new CrashReportCategory(categoryName);
        this.categories.add(category);
        return category;
    }

    public String getCompleteReport() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        
        pw.println("---- Minecraft Crash Report ----");
        pw.println("// ");
        pw.println("// WARNING: If you're reading this, it means the game crashed.");
        pw.println("// ");
        pw.println("");
        pw.println("Time: " + new java.util.Date(timestamp));
        pw.println("Description: " + title);
        pw.println("");
        pw.println("");
        pw.println("Stacktrace:");
        if (cause != null) {
            cause.printStackTrace(pw);
        }
        pw.println("");
        
        for (CrashReportCategory category : categories) {
            pw.println("");
            pw.println("Stacktrace:");
            pw.println(category.toString());
        }
        
        pw.println("");
        pw.println("Affected level:");
        pw.println("   Level name: <unknown>");
        pw.println("   All players: <none>");
        pw.println("   Chunk stats: <unknown>");
        pw.println("   Force-gamedid: <unknown>");
        pw.println("   Level seed: <unknown>");
        pw.println("   Level generator: <unknown>");
        pw.println("   Level generator settings: <none>");
        
        return sw.toString();
    }

    public File getFile() {
        return this.file;
    }

    public boolean saveToFile(File file) {
        this.file = file;
        if (file == null) return false;
        
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(getCompleteReport());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return this.title == null ? "CrashReport" : this.title;
    }
}
