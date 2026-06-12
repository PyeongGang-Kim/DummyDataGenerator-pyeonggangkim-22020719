package org.example.generator;

import java.util.Map;
import java.util.Random;

public class LongValueGenerator implements ValueGenerator<Long> {

    private static final long DEFAULT_MIN = 0L;
    private static final long DEFAULT_MAX = Long.MAX_VALUE / 2;
    private final Random random = new Random();

    @Override
    public Long generate(Map<String, Object> options) {
        long min = ((Number) options.getOrDefault("min", DEFAULT_MIN)).longValue();
        long max = ((Number) options.getOrDefault("max", DEFAULT_MAX)).longValue();
        return min + (long) (random.nextDouble() * (max - min + 1));
    }
}
