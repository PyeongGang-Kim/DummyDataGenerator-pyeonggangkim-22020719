package org.example.generator;

import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

public class LocalDateValueGenerator implements ValueGenerator<LocalDate> {

    private static final LocalDate DEFAULT_FROM = LocalDate.of(2000, 1, 1);
    private static final LocalDate DEFAULT_TO = LocalDate.of(2030, 12, 31);
    private final Random random = new Random();

    @Override
    public LocalDate generate(Map<String, Object> options) {
        LocalDate from = options.containsKey("from")
                ? LocalDate.parse((String) options.get("from"))
                : DEFAULT_FROM;
        LocalDate to = options.containsKey("to")
                ? LocalDate.parse((String) options.get("to"))
                : DEFAULT_TO;
        long fromEpoch = from.toEpochDay();
        long toEpoch = to.toEpochDay();
        long randomEpoch = fromEpoch + (long) (random.nextDouble() * (toEpoch - fromEpoch + 1));
        return LocalDate.ofEpochDay(Math.min(randomEpoch, toEpoch));
    }
}
