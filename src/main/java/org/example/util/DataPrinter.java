package org.example.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataPrinter {

    private static final int PREVIEW_LIMIT = 10;

    public void print(List<Map<String, Object>> rows) {
        if (rows.isEmpty()) {
            System.out.println("(데이터 없음)");
            return;
        }

        List<String> headers = new ArrayList<>(rows.get(0).keySet());
        List<Map<String, Object>> printRows = rows.size() > PREVIEW_LIMIT
                ? rows.subList(0, PREVIEW_LIMIT)
                : rows;

        int[] widths = columnWidths(headers, printRows);
        String border = buildBorder(widths, "┌", "┬", "┐", "─");
        String divider = buildBorder(widths, "├", "┼", "┤", "─");
        String footer  = buildBorder(widths, "└", "┴", "┘", "─");

        System.out.println(border);
        System.out.println(buildRow(headers, widths));
        System.out.println(divider);
        for (Map<String, Object> row : printRows) {
            List<String> values = new ArrayList<>();
            for (String header : headers) {
                Object val = row.get(header);
                values.add(val == null ? "" : val.toString());
            }
            System.out.println(buildRow(values, widths));
        }
        System.out.println(footer);

        if (rows.size() > PREVIEW_LIMIT) {
            System.out.println("... (총 " + rows.size() + "건, 앞 " + PREVIEW_LIMIT + "건만 표시)");
        }
    }

    private int[] columnWidths(List<String> headers, List<Map<String, Object>> rows) {
        int[] widths = new int[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            widths[i] = headers.get(i).length();
        }
        for (Map<String, Object> row : rows) {
            for (int i = 0; i < headers.size(); i++) {
                Object val = row.get(headers.get(i));
                int len = val == null ? 0 : val.toString().length();
                if (len > widths[i]) widths[i] = len;
            }
        }
        return widths;
    }

    private String buildBorder(int[] widths, String left, String mid, String right, String fill) {
        StringBuilder sb = new StringBuilder(left);
        for (int i = 0; i < widths.length; i++) {
            sb.append(fill.repeat(widths[i] + 2));
            sb.append(i < widths.length - 1 ? mid : right);
        }
        return sb.toString();
    }

    private String buildRow(List<String> values, int[] widths) {
        StringBuilder sb = new StringBuilder("│");
        for (int i = 0; i < values.size(); i++) {
            sb.append(" ").append(padRight(values.get(i), widths[i])).append(" │");
        }
        return sb.toString();
    }

    private String padRight(String value, int width) {
        if (value.length() >= width) return value;
        return value + " ".repeat(width - value.length());
    }
}
