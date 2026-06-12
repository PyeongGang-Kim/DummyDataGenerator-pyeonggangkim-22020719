package org.example.generator;

import java.util.Map;
import java.util.Random;

public class IntegerValueGenerator implements ValueGenerator<Integer> {

    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = Integer.MAX_VALUE;
    private final Random random = new Random();

    @Override
    public Integer generate(Map<String, Object> options) {
        int min = ((Number) options.getOrDefault("min", DEFAULT_MIN)).intValue();
        int max = ((Number) options.getOrDefault("max", DEFAULT_MAX)).intValue();
        return (int) (min + (long) (random.nextDouble() * ((long) max - min + 1)));
    }
}
