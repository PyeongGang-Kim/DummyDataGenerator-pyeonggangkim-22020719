package org.example.generator;

import org.example.generator.domain.*;
import org.example.model.FieldType;

import java.util.EnumMap;
import java.util.Map;

public class GeneratorRegistry {

    private final Map<FieldType, ValueGenerator<?>> registry = new EnumMap<>(FieldType.class);

    public GeneratorRegistry() {
        registry.put(FieldType.STRING,         new StringValueGenerator());
        registry.put(FieldType.INTEGER,        new IntegerValueGenerator());
        registry.put(FieldType.LONG,           new LongValueGenerator());
        registry.put(FieldType.DOUBLE,         new DoubleValueGenerator());
        registry.put(FieldType.BOOLEAN,        new BooleanValueGenerator());
        registry.put(FieldType.LOCAL_DATE,     new LocalDateValueGenerator());
        registry.put(FieldType.LOCAL_DATETIME, new LocalDateTimeValueGenerator());
        registry.put(FieldType.UUID,           new UuidValueGenerator());
        registry.put(FieldType.KOREAN_NAME,    new KoreanNameValueGenerator());
        registry.put(FieldType.EMAIL,          new EmailValueGenerator());
        registry.put(FieldType.PHONE,          new PhoneValueGenerator());
        registry.put(FieldType.ADDRESS,        new AddressValueGenerator());
        registry.put(FieldType.NUMERIC_CODE,   new NumericCodeValueGenerator());
    }

    public void register(FieldType type, ValueGenerator<?> generator) {
        registry.put(type, generator);
    }

    public ValueGenerator<?> get(FieldType type) {
        ValueGenerator<?> generator = registry.get(type);
        if (generator == null) {
            throw new IllegalArgumentException("등록된 생성기가 없습니다: " + type);
        }
        return generator;
    }
}
