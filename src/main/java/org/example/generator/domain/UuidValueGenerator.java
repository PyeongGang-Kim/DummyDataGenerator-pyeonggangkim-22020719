package org.example.generator.domain;

import org.example.generator.ValueGenerator;

import java.util.Map;
import java.util.UUID;

public class UuidValueGenerator implements ValueGenerator<String> {

    @Override
    public String generate(Map<String, Object> options) {
        return UUID.randomUUID().toString();
    }
}
