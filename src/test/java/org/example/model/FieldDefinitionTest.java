package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FieldDefinitionTest {

    @Test
    void 필드명과_타입만으로_생성할_수_있다() {
        FieldDefinition field = FieldDefinition.of("id", FieldType.UUID);

        assertEquals("id", field.getName());
        assertEquals(FieldType.UUID, field.getType());
    }

    @Test
    void 옵션_없이_생성하면_빈_맵을_반환한다() {
        FieldDefinition field = FieldDefinition.of("name", FieldType.KOREAN_NAME);

        assertTrue(field.getOptions().isEmpty());
    }

    @Test
    void 옵션과_함께_생성할_수_있다() {
        Map<String, Object> options = Map.of("min", 20, "max", 60);
        FieldDefinition field = FieldDefinition.of("age", FieldType.INTEGER, options);

        assertEquals(20, field.getOptions().get("min"));
        assertEquals(60, field.getOptions().get("max"));
    }

    @Test
    void 필드명이_null이면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldDefinition.of(null, FieldType.STRING));
    }

    @Test
    void 타입이_null이면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldDefinition.of("name", null));
    }
}
