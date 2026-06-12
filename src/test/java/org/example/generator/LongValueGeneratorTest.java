package org.example.generator;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LongValueGeneratorTest {

    private final LongValueGenerator generator = new LongValueGenerator();

    @Test
    void min_max_범위_내의_값을_생성한다() {
        for (int i = 0; i < 50; i++) {
            long value = generator.generate(Map.of("min", 1_000_000L, "max", 9_999_999L));
            assertTrue(value >= 1_000_000L && value <= 9_999_999L);
        }
    }

    @Test
    void 옵션_없이_생성해도_예외가_발생하지_않는다() {
        assertDoesNotThrow(() -> generator.generate(Collections.emptyMap()));
    }

    @Test
    void int_범위를_초과하는_큰_값도_생성할_수_있다() {
        long min = (long) Integer.MAX_VALUE + 1;
        long max = (long) Integer.MAX_VALUE + 1000;
        long value = generator.generate(Map.of("min", min, "max", max));

        assertTrue(value >= min && value <= max);
    }
}
