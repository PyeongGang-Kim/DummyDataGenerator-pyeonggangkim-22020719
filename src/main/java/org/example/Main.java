package org.example;

import org.example.generator.EntityGenerator;
import org.example.generator.GeneratorRegistry;
import org.example.model.FieldDefinition;
import org.example.model.FieldType;
import org.example.store.InMemoryDataStore;
import org.example.util.DataPrinter;
import org.example.util.DataValidator;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        GeneratorRegistry registry = new GeneratorRegistry();
        InMemoryDataStore store = new InMemoryDataStore();
        DataPrinter printer = new DataPrinter();
        DataValidator validator = new DataValidator();

        runScenarioA(registry, store, printer, validator);
        runScenarioB(registry, store, printer, validator);
        runPerformanceCheck(registry);
    }

    private static void runScenarioA(
            GeneratorRegistry registry,
            InMemoryDataStore store,
            DataPrinter printer,
            DataValidator validator) {

        System.out.println("=== 시나리오 A: 사용자 테이블 1,000건 ===");

        List<FieldDefinition> fields = List.of(
                FieldDefinition.of("id",       FieldType.UUID),
                FieldDefinition.of("name",     FieldType.KOREAN_NAME),
                FieldDefinition.of("email",    FieldType.EMAIL),
                FieldDefinition.of("phone",    FieldType.PHONE),
                FieldDefinition.of("age",      FieldType.INTEGER, Map.of("min", 20, "max", 60)),
                FieldDefinition.of("joinDate", FieldType.LOCAL_DATE,
                        Map.of("from", "2015-01-01", "to", "2024-12-31"))
        );

        EntityGenerator generator = new EntityGenerator(fields, registry);
        List<Map<String, Object>> rows = generator.generate(1000);

        validator.validateCount(rows, 1000);
        validator.validateRequiredFields(rows, List.of("id", "name", "email", "phone", "age", "joinDate"));

        store.save("users", rows);

        System.out.println("생성 건수: " + store.size("users") + "건");
        printer.print(store.get("users"));
        System.out.println();
    }

    private static void runScenarioB(
            GeneratorRegistry registry,
            InMemoryDataStore store,
            DataPrinter printer,
            DataValidator validator) {

        System.out.println("=== 시나리오 B: 주문 테이블 500건 ===");

        List<FieldDefinition> fields = List.of(
                FieldDefinition.of("orderId",   FieldType.NUMERIC_CODE, Map.of("digits", 8)),
                FieldDefinition.of("userId",    FieldType.UUID),
                FieldDefinition.of("amount",    FieldType.DOUBLE,
                        Map.of("min", 1000.0, "max", 500000.0, "scale", 0)),
                FieldDefinition.of("orderDate", FieldType.LOCAL_DATETIME,
                        Map.of("from", "2023-01-01T00:00:00", "to", "2024-12-31T23:59:59")),
                FieldDefinition.of("isPaid",    FieldType.BOOLEAN),
                FieldDefinition.of("address",   FieldType.ADDRESS)
        );

        EntityGenerator generator = new EntityGenerator(fields, registry);
        List<Map<String, Object>> rows = generator.generate(500);

        validator.validateCount(rows, 500);
        validator.validateRequiredFields(rows,
                List.of("orderId", "userId", "amount", "orderDate", "isPaid", "address"));

        store.save("orders", rows);

        System.out.println("생성 건수: " + store.size("orders") + "건");
        printer.print(store.get("orders"));
        System.out.println();
    }

    private static void runPerformanceCheck(GeneratorRegistry registry) {
        System.out.println("=== 성능 체크: 10만 건 생성 ===");

        List<FieldDefinition> fields = List.of(
                FieldDefinition.of("id",    FieldType.UUID),
                FieldDefinition.of("name",  FieldType.KOREAN_NAME),
                FieldDefinition.of("email", FieldType.EMAIL),
                FieldDefinition.of("age",   FieldType.INTEGER, Map.of("min", 1, "max", 100))
        );

        EntityGenerator generator = new EntityGenerator(fields, registry);
        long start = System.currentTimeMillis();
        List<Map<String, Object>> rows = generator.generate(100_000);
        long elapsed = System.currentTimeMillis() - start;

        System.out.println("생성 건수: " + rows.size() + "건");
        System.out.println("소요 시간: " + elapsed + "ms (기준: 10,000ms 이하)");
        System.out.println("성능 기준 " + (elapsed <= 10_000 ? "통과" : "미달"));
    }
}
