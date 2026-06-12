package org.example.generator;

import org.example.model.FieldDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityGenerator {

    private final List<FieldDefinition> fields;
    private final GeneratorRegistry registry;

    public EntityGenerator(List<FieldDefinition> fields, GeneratorRegistry registry) {
        this.fields = fields;
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> generate(int count) {
        List<Map<String, Object>> rows = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Map<String, Object> row = new HashMap<>();
            for (FieldDefinition field : fields) {
                ValueGenerator<Object> generator = (ValueGenerator<Object>) registry.get(field.getType());
                row.put(field.getName(), generator.generate(field.getOptions()));
            }
            rows.add(row);
        }
        return rows;
    }
}
