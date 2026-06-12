package org.example.generator.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NumericCodeValueGeneratorTest {

    private final NumericCodeValueGenerator generator = new NumericCodeValueGenerator();

    @Test
    void 기본_6자리_숫자_코드를_생성한다() {
        String value = generator.generate(Collections.emptyMap());

        assertEquals(6, value.length());
    }

    @Test
    void digits_옵션으로_자릿수를_지정할_수_있다() {
        String value = generator.generate(Map.of("digits", 8));

        assertEquals(8, value.length());
    }

    @Test
    void 숫자로만_구성된다() {
        for (int i = 0; i < 20; i++) {
            String value = generator.generate(Collections.emptyMap());
            assertTrue(value.matches("\\d+"), "숫자 외 문자 포함: " + value);
        }
    }

    @Test
    void 자릿수에_맞게_zero_padding이_적용된다() {
        String value = generator.generate(Map.of("digits", 6));

        assertEquals(6, value.length());
    }
}
