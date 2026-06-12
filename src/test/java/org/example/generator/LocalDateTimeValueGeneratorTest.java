package org.example.generator;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateTimeValueGeneratorTest {

    private final LocalDateTimeValueGenerator generator = new LocalDateTimeValueGenerator();

    @Test
    void from_to_범위_내의_일시를_생성한다() {
        LocalDateTime from = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 12, 31, 23, 59, 59);

        for (int i = 0; i < 20; i++) {
            LocalDateTime value = generator.generate(
                    Map.of("from", "2020-01-01T00:00:00", "to", "2024-12-31T23:59:59"));
            assertFalse(value.isBefore(from));
            assertFalse(value.isAfter(to));
        }
    }

    @Test
    void 옵션_없이_생성해도_예외가_발생하지_않는다() {
        assertDoesNotThrow(() -> generator.generate(Collections.emptyMap()));
    }

    @Test
    void 초_단위까지_포함한_일시를_반환한다() {
        LocalDateTime value = generator.generate(Collections.emptyMap());

        assertNotNull(value.toLocalDate());
        assertNotNull(value.toLocalTime());
    }
}
