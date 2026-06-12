package org.example.generator;

import java.util.Map;
import java.util.Random;

public class StringValueGenerator implements ValueGenerator<String> {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Random random = new Random();

    @Override
    public String generate(Map<String, Object> options) {
        int length = (int) options.getOrDefault("length", 8);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
