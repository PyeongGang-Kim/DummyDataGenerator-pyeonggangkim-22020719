package org.example.generator.domain;

import org.example.generator.ValueGenerator;

import java.util.Map;
import java.util.Random;

public class EmailValueGenerator implements ValueGenerator<String> {

    private static final String[] DOMAINS = {
        "gmail.com", "naver.com", "kakao.com", "daum.net", "outlook.com"
    };
    private static final String LOCAL_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LOCAL_LENGTH = 8;

    private final Random random = new Random();

    @Override
    public String generate(Map<String, Object> options) {
        StringBuilder local = new StringBuilder(LOCAL_LENGTH);
        for (int i = 0; i < LOCAL_LENGTH; i++) {
            local.append(LOCAL_CHARS.charAt(random.nextInt(LOCAL_CHARS.length())));
        }
        String domain = DOMAINS[random.nextInt(DOMAINS.length)];
        return local + "@" + domain;
    }
}
