package org.example.generator.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UuidValueGeneratorTest {

    private final UuidValueGenerator generator = new UuidValueGenerator();

    @Test
    void 표준_UUID_형식의_문자열을_생성한다() {
        String value = generator.generate(Collections.emptyMap());

        assertDoesNotThrow(() -> UUID.fromString(value));
    }

    @Test
    void 반복_호출_시_고유한_값을_생성한다() {
        long distinctCount = java.util.stream.Stream
                .generate(() -> generator.generate(Collections.emptyMap()))
                .limit(10)
                .distinct()
                .count();

        assertEquals(10, distinctCount);
    }
}
