package org.example.generator;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StringValueGeneratorTest {

    private final StringValueGenerator generator = new StringValueGenerator();

    @Test
    void 기본_길이_8의_문자열을_생성한다() {
        String value = generator.generate(Collections.emptyMap());

        assertEquals(8, value.length());
    }

    @Test
    void length_옵션으로_길이를_지정할_수_있다() {
        String value = generator.generate(Map.of("length", 16));

        assertEquals(16, value.length());
    }

    @Test
    void 반복_호출_시_다양한_값을_생성한다() {
        long distinctCount = java.util.stream.Stream
                .generate(() -> generator.generate(Collections.emptyMap()))
                .limit(10)
                .distinct()
                .count();

        assertTrue(distinctCount > 1);
    }
}
