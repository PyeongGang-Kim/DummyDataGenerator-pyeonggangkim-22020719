package org.example.generator;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateValueGeneratorTest {

    private final LocalDateValueGenerator generator = new LocalDateValueGenerator();

    @Test
    void from_to_범위_내의_날짜를_생성한다() {
        LocalDate from = LocalDate.of(2020, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);

        for (int i = 0; i < 20; i++) {
            LocalDate value = generator.generate(Map.of("from", "2020-01-01", "to", "2024-12-31"));
            assertFalse(value.isBefore(from));
            assertFalse(value.isAfter(to));
        }
    }

    @Test
    void 옵션_없이_생성해도_예외가_발생하지_않는다() {
        assertDoesNotThrow(() -> generator.generate(Collections.emptyMap()));
    }

    @Test
    void from과_to가_같으면_해당_날짜만_반환한다() {
        LocalDate value = generator.generate(Map.of("from", "2024-06-01", "to", "2024-06-01"));

        assertEquals(LocalDate.of(2024, 6, 1), value);
    }
}
