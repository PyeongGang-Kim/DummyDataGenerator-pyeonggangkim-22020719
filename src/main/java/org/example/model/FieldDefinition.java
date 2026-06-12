package org.example.model;

import java.util.Collections;
import java.util.Map;

public class FieldDefinition {

    private final String name;
    private final FieldType type;
    private final Map<String, Object> options;

    private FieldDefinition(String name, FieldType type, Map<String, Object> options) {
        this.name = name;
        this.type = type;
        this.options = options;
    }

    public static FieldDefinition of(String name, FieldType type) {
        return of(name, type, Collections.emptyMap());
    }

    public static FieldDefinition of(String name, FieldType type, Map<String, Object> options) {
        if (name == null) throw new IllegalArgumentException("name must not be null");
        if (type == null) throw new IllegalArgumentException("type must not be null");
        return new FieldDefinition(name, type, options == null ? Collections.emptyMap() : options);
    }

    public String getName() { return name; }
    public FieldType getType() { return type; }
    public Map<String, Object> getOptions() { return options; }
}
