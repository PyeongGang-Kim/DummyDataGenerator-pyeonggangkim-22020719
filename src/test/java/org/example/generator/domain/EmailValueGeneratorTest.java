package org.example.generator.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class EmailValueGeneratorTest {

    private final EmailValueGenerator generator = new EmailValueGenerator();

    @Test
    void at_기호를_포함한_이메일을_생성한다() {
        String value = generator.generate(Collections.emptyMap());

        assertTrue(value.contains("@"));
    }

    @Test
    void 로컬파트와_도메인으로_구성된_형식을_가진다() {
        String value = generator.generate(Collections.emptyMap());
        String[] parts = value.split("@");

        assertEquals(2, parts.length);
        assertFalse(parts[0].isEmpty());
        assertTrue(parts[1].contains("."));
    }

    @Test
    void 반복_호출_시_다양한_이메일을_생성한다() {
        long distinctCount = java.util.stream.Stream
                .generate(() -> generator.generate(Collections.emptyMap()))
                .limit(10)
                .distinct()
                .count();

        assertTrue(distinctCount > 1);
    }
}
