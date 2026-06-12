package org.example.generator.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AddressValueGeneratorTest {

    private final AddressValueGenerator generator = new AddressValueGenerator();

    @Test
    void 공백으로_구분된_시_구_동을_포함한_주소를_생성한다() {
        String value = generator.generate(Collections.emptyMap());
        String[] parts = value.split(" ");

        assertTrue(parts.length >= 3);
    }

    @Test
    void 반복_호출_시_다양한_주소를_생성한다() {
        long distinctCount = java.util.stream.Stream
                .generate(() -> generator.generate(Collections.emptyMap()))
                .limit(20)
                .distinct()
                .count();

        assertTrue(distinctCount > 1);
    }
}
