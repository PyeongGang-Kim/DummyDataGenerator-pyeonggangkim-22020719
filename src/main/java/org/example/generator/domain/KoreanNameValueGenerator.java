package org.example.generator.domain;

import org.example.generator.ValueGenerator;

import java.util.Map;
import java.util.Random;

public class KoreanNameValueGenerator implements ValueGenerator<String> {

    private static final String[] LAST_NAMES = {
        "김", "이", "박", "최", "정", "강", "조", "윤", "장", "임",
        "한", "오", "서", "신", "권", "황", "안", "송", "류", "전",
        "홍", "고", "문", "양", "손"
    };

    private static final String[] NAME_CHARS = {
        "민", "서", "예", "지", "현", "수", "진", "유", "은", "재",
        "준", "혜", "연", "승", "희", "경", "나", "도", "하", "미",
        "영", "정", "주", "아", "원", "윤", "태", "선", "빛", "찬"
    };

    private final Random random = new Random();

    @Override
    public String generate(Map<String, Object> options) {
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String firstName = NAME_CHARS[random.nextInt(NAME_CHARS.length)]
                + NAME_CHARS[random.nextInt(NAME_CHARS.length)];
        return lastName + firstName;
    }
}
