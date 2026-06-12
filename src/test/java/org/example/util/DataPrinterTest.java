package org.example.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataPrinterTest {

    private final DataPrinter printer = new DataPrinter();
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void 빈_리스트를_전달해도_예외가_발생하지_않는다() {
        assertDoesNotThrow(() -> printer.print(List.of()));
    }

    @Test
    void 출력_결과에_헤더_필드명이_포함된다() {
        printer.print(List.of(Map.of("id", "1", "name", "김민준")));
        String result = output.toString();

        assertTrue(result.contains("id"));
        assertTrue(result.contains("name"));
    }

    @Test
    void 출력_결과에_데이터_값이_포함된다() {
        printer.print(List.of(Map.of("id", "abc-123")));
        String result = output.toString();

        assertTrue(result.contains("abc-123"));
    }

    @Test
    void _100건_초과_시_총_N건_메시지가_출력된다() {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            rows.add(Map.of("id", String.valueOf(i)));
        }
        printer.print(rows);
        String result = output.toString();

        assertTrue(result.contains("101"));
    }
}
