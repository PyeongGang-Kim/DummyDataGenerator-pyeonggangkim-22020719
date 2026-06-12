package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataSetTest {

    @Test
    void 레이블과_rows로_생성할_수_있다() {
        List<Map<String, Object>> rows = List.of(
                Map.of("id", "1", "name", "김민준")
        );
        DataSet dataSet = new DataSet("users", rows);

        assertEquals("users", dataSet.getLabel());
        assertEquals(1, dataSet.size());
    }

    @Test
    void 빈_rows로_생성하면_size는_0이다() {
        DataSet dataSet = new DataSet("empty", List.of());

        assertEquals(0, dataSet.size());
    }

    @Test
    void getRows는_저장된_데이터를_반환한다() {
        Map<String, Object> row = Map.of("id", "1");
        DataSet dataSet = new DataSet("test", List.of(row));

        assertEquals(1, dataSet.getRows().size());
        assertEquals("1", dataSet.getRows().get(0).get("id"));
    }

    @Test
    void 레이블이_null이면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSet(null, List.of()));
    }

    @Test
    void rows가_null이면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class,
                () -> new DataSet("users", null));
    }
}
