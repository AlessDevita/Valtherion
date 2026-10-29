package fr.alessdevita.valtherion.stats;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatFileWriter {
    private final Map<String, Integer> statistics = new ConcurrentHashMap<>();
    public void increaseStat(String id, int amount) { if (id != null && amount != 0) statistics.merge(id, amount, Integer::sum); }
    public int readStat(String id) { return statistics.getOrDefault(id, 0); }
    public Map<String, Integer> snapshot() { return Collections.unmodifiableMap(new java.util.HashMap<>(statistics)); }
}
