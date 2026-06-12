package org.example.generator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Random;

public class LocalDateTimeValueGenerator implements ValueGenerator<LocalDateTime> {

    private static final LocalDateTime DEFAULT_FROM = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
    private static final LocalDateTime DEFAULT_TO = LocalDateTime.of(2030, 12, 31, 23, 59, 59);
    private final Random random = new Random();

    @Override
    public LocalDateTime generate(Map<String, Object> options) {
        LocalDateTime from = options.containsKey("from")
                ? LocalDateTime.parse((String) options.get("from"))
                : DEFAULT_FROM;
        LocalDateTime to = options.containsKey("to")
                ? LocalDateTime.parse((String) options.get("to"))
                : DEFAULT_TO;
        long fromEpoch = from.toEpochSecond(ZoneOffset.UTC);
        long toEpoch = to.toEpochSecond(ZoneOffset.UTC);
        long randomEpoch = fromEpoch + (long) (random.nextDouble() * (toEpoch - fromEpoch + 1));
        return LocalDateTime.ofEpochSecond(Math.min(randomEpoch, toEpoch), 0, ZoneOffset.UTC);
    }
}
