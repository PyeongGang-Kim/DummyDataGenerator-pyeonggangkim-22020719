package org.example.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDataStoreTest {

    private InMemoryDataStore store;

    @BeforeEach
    void setUp() {
        store = new InMemoryDataStore();
    }

    @Test
    void 저장한_데이터를_레이블로_조회할_수_있다() {
        List<Map<String, Object>> rows = List.of(Map.of("id", "1"));
        store.save("users", rows);

        assertEquals(1, store.get("users").size());
    }

    @Test
    void 동일_레이블로_다시_저장하면_덮어쓴다() {
        store.save("users", List.of(Map.of("id", "1"), Map.of("id", "2")));
        store.save("users", List.of(Map.of("id", "3")));

        assertEquals(1, store.get("users").size());
    }

    @Test
    void append는_기존_데이터에_추가한다() {
        store.save("users", List.of(Map.of("id", "1")));
        store.append("users", List.of(Map.of("id", "2"), Map.of("id", "3")));

        assertEquals(3, store.get("users").size());
    }

    @Test
    void 없는_레이블_조회_시_빈_리스트를_반환한다() {
        List<Map<String, Object>> result = store.get("nonexistent");

        assertTrue(result.isEmpty());
    }

    @Test
    void labels는_저장된_레이블_목록을_반환한다() {
        store.save("users", List.of());
        store.save("orders", List.of());

        assertTrue(store.labels().contains("users"));
        assertTrue(store.labels().contains("orders"));
    }

    @Test
    void remove는_특정_데이터셋을_삭제한다() {
        store.save("users", List.of(Map.of("id", "1")));
        store.remove("users");

        assertTrue(store.get("users").isEmpty());
    }

    @Test
    void clear는_전체_데이터를_삭제한다() {
        store.save("users", List.of(Map.of("id", "1")));
        store.save("orders", List.of(Map.of("id", "2")));
        store.clear();

        assertTrue(store.labels().isEmpty());
    }

    @Test
    void size는_특정_데이터셋의_건수를_반환한다() {
        store.save("users", List.of(Map.of("id", "1"), Map.of("id", "2"), Map.of("id", "3")));

        assertEquals(3, store.size("users"));
    }

    @Test
    void append_대상_레이블이_없으면_새로_생성한다() {
        store.append("users", List.of(Map.of("id", "1")));

        assertEquals(1, store.get("users").size());
    }
}
