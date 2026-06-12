package org.example.generator.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PhoneValueGeneratorTest {

    private final PhoneValueGenerator generator = new PhoneValueGenerator();

    @Test
    void _010으로_시작하는_전화번호를_생성한다() {
        String value = generator.generate(Collections.emptyMap());

        assertTrue(value.startsWith("010-"));
    }

    @Test
    void _010_XXXX_XXXX_형식을_따른다() {
        for (int i = 0; i < 20; i++) {
            String value = generator.generate(Collections.emptyMap());
            assertTrue(value.matches("010-\\d{4}-\\d{4}"),
                    "형식 불일치: " + value);
        }
    }
}
