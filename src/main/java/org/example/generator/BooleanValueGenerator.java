package org.example.generator;

import java.util.Map;
import java.util.Random;

public class BooleanValueGenerator implements ValueGenerator<Boolean> {

    private final Random random = new Random();

    @Override
    public Boolean generate(Map<String, Object> options) {
        return random.nextBoolean();
    }
}
