package org.example.generator;

import org.example.model.FieldDefinition;
import org.example.model.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EntityGeneratorTest {

    private GeneratorRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new GeneratorRegistry();
    }

    @Test
    void 지정한_건수만큼_레코드를_생성한다() {
        List<FieldDefinition> fields = List.of(
                FieldDefinition.of("id", FieldType.UUID)
        );
        EntityGenerator generator = new EntityGenerator(fields, registry);

        List<Map<String, Object>> rows = generator.generate(100);

        assertEquals(100, rows.size());
    }

    @Test
    void 생성된_레코드에_정의된_모든_필드가_포함된다() {
        List<FieldDefinition> fields = List.of(
                FieldDefinition.of("id",    FieldType.UUID),
                FieldDefinition.of("name",  FieldType.KOREAN_NAME),
                FieldDefinition.of("email", FieldType.EMAIL)
        );
        EntityGenerator generator = new EntityGenerator(fields, registry);

        Map<String, Object> row = generator.generate(1).get(0);

        assertTrue(row.containsKey("id"));
        assertTrue(row.containsKey("name"));
        assertTrue(row.containsKey("email"));
    }

    @Test
    void 필드_옵션이_생성_값에_반영된다() {
        List<FieldDefinition> fields = List.of(
                FieldDefinition.of("age", FieldType.INTEGER, Map.of("min", 20, "max", 20))
        );
        EntityGenerator generator = new EntityGenerator(fields, registry);

        Map<String, Object> row = generator.generate(1).get(0);

        assertEquals(20, row.get("age"));
    }

    @Test
    void 건수가_0이면_빈_리스트를_반환한다() {
        List<FieldDefinition> fields = List.of(
                FieldDefinition.of("id", FieldType.UUID)
        );
        EntityGenerator generator = new EntityGenerator(fields, registry);

        List<Map<String, Object>> rows = generator.generate(0);

        assertTrue(rows.isEmpty());
    }

    @Test
    void 필드_정의가_없으면_빈_맵_레코드를_생성한다() {
        EntityGenerator generator = new EntityGenerator(List.of(), registry);

        List<Map<String, Object>> rows = generator.generate(3);

        assertEquals(3, rows.size());
        rows.forEach(row -> assertTrue(row.isEmpty()));
    }
}
