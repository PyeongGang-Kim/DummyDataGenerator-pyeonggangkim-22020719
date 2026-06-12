package org.example.generator.domain;

import org.example.generator.ValueGenerator;

import java.util.Map;
import java.util.Random;

public class NumericCodeValueGenerator implements ValueGenerator<String> {

    private static final int DEFAULT_DIGITS = 6;
    private final Random random = new Random();

    @Override
    public String generate(Map<String, Object> options) {
        int digits = ((Number) options.getOrDefault("digits", DEFAULT_DIGITS)).intValue();
        int max = (int) Math.pow(10, digits);
        return String.format("%0" + digits + "d", random.nextInt(max));
    }
}
