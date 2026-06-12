package org.example.generator.domain;

import org.example.generator.ValueGenerator;

import java.util.Map;
import java.util.Random;

public class AddressValueGenerator implements ValueGenerator<String> {

    private static final String[] CITIES = {
        "서울특별시", "부산광역시", "인천광역시", "대구광역시", "대전광역시"
    };

    private static final String[] DISTRICTS = {
        "강남구", "강북구", "마포구", "송파구", "종로구",
        "해운대구", "부산진구", "남구", "수성구", "달서구"
    };

    private static final String[] NEIGHBORHOODS = {
        "역삼동", "삼성동", "서교동", "합정동", "신사동",
        "중동", "좌동", "범어동", "시지동", "죽전동"
    };

    private final Random random = new Random();

    @Override
    public String generate(Map<String, Object> options) {
        String city = CITIES[random.nextInt(CITIES.length)];
        String district = DISTRICTS[random.nextInt(DISTRICTS.length)];
        String neighborhood = NEIGHBORHOODS[random.nextInt(NEIGHBORHOODS.length)];
        return city + " " + district + " " + neighborhood;
    }
}
