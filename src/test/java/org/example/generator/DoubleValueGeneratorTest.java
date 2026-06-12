package org.example.generator;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DoubleValueGeneratorTest {

    private final DoubleValueGenerator generator = new DoubleValueGenerator();

    @Test
    void min_max_범위_내의_값을_생성한다() {
        for (int i = 0; i < 50; i++) {
            double value = generator.generate(Map.of("min", 1.0, "max", 10.0));
            assertTrue(value >= 1.0 && value <= 10.0);
        }
    }

    @Test
    void scale_옵션으로_소수점_자리수를_지정할_수_있다() {
        double value = generator.generate(Map.of("min", 0.0, "max", 100.0, "scale", 2));
        String str = String.valueOf(value);
        int dotIndex = str.indexOf('.');
        int actualScale = dotIndex < 0 ? 0 : str.length() - dotIndex - 1;

        assertTrue(actualScale <= 2);
    }

    @Test
    void 옵션_없이_생성해도_예외가_발생하지_않는다() {
        assertDoesNotThrow(() -> generator.generate(Collections.emptyMap()));
    }
}
