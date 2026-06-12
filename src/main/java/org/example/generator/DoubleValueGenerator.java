package org.example.generator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;

public class DoubleValueGenerator implements ValueGenerator<Double> {

    private static final double DEFAULT_MIN = 0.0;
    private static final double DEFAULT_MAX = 1_000_000.0;
    private static final int DEFAULT_SCALE = 2;
    private final Random random = new Random();

    @Override
    public Double generate(Map<String, Object> options) {
        double min = ((Number) options.getOrDefault("min", DEFAULT_MIN)).doubleValue();
        double max = ((Number) options.getOrDefault("max", DEFAULT_MAX)).doubleValue();
        int scale = ((Number) options.getOrDefault("scale", DEFAULT_SCALE)).intValue();
        double raw = min + random.nextDouble() * (max - min);
        return BigDecimal.valueOf(raw).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }
}
