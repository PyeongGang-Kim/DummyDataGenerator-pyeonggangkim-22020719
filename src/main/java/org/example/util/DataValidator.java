package org.example.util;

import java.util.List;
import java.util.Map;

public class DataValidator {

    public void validateCount(List<?> rows, int expected) {
        if (rows.size() != expected) {
            throw new IllegalStateException(
                    "건수 불일치: 기대=" + expected + ", 실제=" + rows.size());
        }
    }

    public void validateRequiredFields(List<Map<String, Object>> rows, List<String> requiredFields) {
        for (int i = 0; i < rows.size(); i++) {
            Map<String, Object> row = rows.get(i);
            for (String field : requiredFields) {
                if (!row.containsKey(field)) {
                    throw new IllegalStateException(
                            "필수 필드 누락: row[" + i + "] 에서 '" + field + "' 필드가 없습니다.");
                }
            }
        }
    }
}
