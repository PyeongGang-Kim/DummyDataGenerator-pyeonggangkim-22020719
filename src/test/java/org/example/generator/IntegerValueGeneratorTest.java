package org.example.generator;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IntegerValueGeneratorTest {

    private final IntegerValueGenerator generator = new IntegerValueGenerator();

    @Test
    void min_max_범위_내의_값을_생성한다() {
        for (int i = 0; i < 50; i++) {
            int value = generator.generate(Map.of("min", 10, "max", 20));
            assertTrue(value >= 10 && value <= 20);
        }
    }

    @Test
    void 옵션_없이_생성해도_예외가_발생하지_않는다() {
        assertDoesNotThrow(() -> generator.generate(Collections.emptyMap()));
    }

    @Test
    void min과_max가_같으면_해당_값만_반환한다() {
        int value = generator.generate(Map.of("min", 5, "max", 5));

        assertEquals(5, value);
    }
}
