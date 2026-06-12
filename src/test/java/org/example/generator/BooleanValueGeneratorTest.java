package org.example.generator;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BooleanValueGeneratorTest {

    private final BooleanValueGenerator generator = new BooleanValueGenerator();

    @Test
    void true_또는_false를_반환한다() {
        Boolean value = generator.generate(Collections.emptyMap());

        assertNotNull(value);
    }

    @Test
    void 반복_호출_시_true와_false를_모두_생성한다() {
        boolean hasTure = false;
        boolean hasFalse = false;

        for (int i = 0; i < 100; i++) {
            boolean value = generator.generate(Collections.emptyMap());
            if (value) hasTure = true;
            else hasFalse = true;
            if (hasTure && hasFalse) break;
        }

        assertTrue(hasTure && hasFalse);
    }
}
