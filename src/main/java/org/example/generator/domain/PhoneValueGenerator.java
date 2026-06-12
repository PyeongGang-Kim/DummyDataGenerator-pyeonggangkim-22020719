package org.example.generator.domain;

import org.example.generator.ValueGenerator;

import java.util.Map;
import java.util.Random;

public class PhoneValueGenerator implements ValueGenerator<String> {

    private final Random random = new Random();

    @Override
    public String generate(Map<String, Object> options) {
        String middle = String.format("%04d", random.nextInt(10000));
        String last = String.format("%04d", random.nextInt(10000));
        return "010-" + middle + "-" + last;
    }
}
