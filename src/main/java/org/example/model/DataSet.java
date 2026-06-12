package org.example.model;

import java.util.List;
import java.util.Map;

public class DataSet {

    private final String label;
    private final List<Map<String, Object>> rows;

    public DataSet(String label, List<Map<String, Object>> rows) {
        if (label == null) throw new IllegalArgumentException("label must not be null");
        if (rows == null) throw new IllegalArgumentException("rows must not be null");
        this.label = label;
        this.rows = rows;
    }

    public String getLabel() { return label; }
    public List<Map<String, Object>> getRows() { return rows; }
    public int size() { return rows.size(); }
}
