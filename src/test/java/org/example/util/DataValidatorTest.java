package org.example.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataValidatorTest {

    private final DataValidator validator = new DataValidator();

    @Test
    void 건수가_일치하면_예외가_발생하지_않는다() {
        List<Map<String, Object>> rows = List.of(Map.of("id", "1"), Map.of("id", "2"));

        assertDoesNotThrow(() -> validator.validateCount(rows, 2));
    }

    @Test
    void 건수가_불일치하면_예외가_발생한다() {
        List<Map<String, Object>> rows = List.of(Map.of("id", "1"));

        assertThrows(IllegalStateException.class,
                () -> validator.validateCount(rows, 5));
    }

    @Test
    void 필수_필드가_모두_존재하면_예외가_발생하지_않는다() {
        List<Map<String, Object>> rows = List.of(
                Map.of("id", "1", "name", "김민준"),
                Map.of("id", "2", "name", "이서윤")
        );

        assertDoesNotThrow(() -> validator.validateRequiredFields(rows, List.of("id", "name")));
    }

    @Test
    void 필수_필드가_누락되면_예외가_발생한다() {
        List<Map<String, Object>> rows = List.of(
                Map.of("id", "1"),
                Map.of("id", "2")
        );

        assertThrows(IllegalStateException.class,
                () -> validator.validateRequiredFields(rows, List.of("id", "name")));
    }

    @Test
    void 빈_리스트는_건수_0으로_검증한다() {
        assertDoesNotThrow(() -> validator.validateCount(List.of(), 0));
    }

    @Test
    void 빈_리스트에_필수_필드_검증은_통과한다() {
        assertDoesNotThrow(() -> validator.validateRequiredFields(List.of(), List.of("id")));
    }
}
