package org.example.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InMemoryDataStore {

    private final Map<String, List<Map<String, Object>>> store = new LinkedHashMap<>();

    public void save(String label, List<Map<String, Object>> rows) {
        store.put(label, new ArrayList<>(rows));
    }

    public void append(String label, List<Map<String, Object>> rows) {
        store.computeIfAbsent(label, k -> new ArrayList<>()).addAll(rows);
    }

    public List<Map<String, Object>> get(String label) {
        return store.getOrDefault(label, Collections.emptyList());
    }

    public Set<String> labels() {
        return store.keySet();
    }

    public void remove(String label) {
        store.remove(label);
    }

    public void clear() {
        store.clear();
    }

    public int size(String label) {
        return get(label).size();
    }
}
