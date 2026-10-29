package fr.alessdevita.valtherion.crash;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CrashReportCategory {
    private final String name;
    private final Map<String, Object> details = new LinkedHashMap<String, Object>();

    public CrashReportCategory(String name) {
        this.name = name == null ? "Unknown" : name;
    }

    public void addCrashSection(String key, Object value) {
        this.details.put(key, value);
    }

    public void addCrashSectionCallable(String key, Callable<?> value) {
        try {
            this.details.put(key, value == null ? null : value.call());
        } catch (Exception exception) {
            this.details.put(key, "Error: " + exception.getMessage());
        }
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Object> getDetails() {
        return new LinkedHashMap<String, Object>(this.details);
    }
}
