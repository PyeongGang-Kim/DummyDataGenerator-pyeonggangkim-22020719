package org.example.generator.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class KoreanNameValueGeneratorTest {

    private final KoreanNameValueGenerator generator = new KoreanNameValueGenerator();

    @Test
    void 성과_이름을_합친_한국어_이름을_생성한다() {
        String value = generator.generate(Collections.emptyMap());

        assertNotNull(value);
        assertTrue(value.length() >= 2);
    }

    @Test
    void 생성된_이름은_한글로만_구성된다() {
        for (int i = 0; i < 20; i++) {
            String value = generator.generate(Collections.emptyMap());
            assertTrue(value.chars().allMatch(c -> c >= '가' && c <= '힣'),
                    "한글이 아닌 문자 포함: " + value);
        }
    }

    @Test
    void 반복_호출_시_다양한_이름을_생성한다() {
        long distinctCount = java.util.stream.Stream
                .generate(() -> generator.generate(Collections.emptyMap()))
                .limit(30)
                .distinct()
                .count();

        assertTrue(distinctCount > 1);
    }
}
