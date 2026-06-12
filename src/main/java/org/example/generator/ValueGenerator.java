package org.example.generator;

import java.util.Map;

public interface ValueGenerator<T> {
    T generate(Map<String, Object> options);
}
