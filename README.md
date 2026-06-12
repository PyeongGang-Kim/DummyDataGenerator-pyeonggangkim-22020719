# DummyDataGenerator

DB 성능 테스트, 통합 테스트, 개발 환경 데이터 세팅에 사용할 더미 데이터를 프로그래밍 방식으로 생성하는 Java PoC 프로젝트입니다.

생성된 데이터는 JVM 메모리에 보관하며, DB 입력은 별도 PoC에서 수행합니다.

## 기술 스택

- Java 17
- Gradle 8 (Kotlin DSL)
- JUnit Jupiter 6
- 외부 라이브러리 없음 (Java 표준 라이브러리만 사용)

## 프로젝트 구조

```
src/
├── main/java/org/example/
│   ├── Main.java                          # 실행 시나리오
│   ├── generator/
│   │   ├── ValueGenerator.java            # 생성기 인터페이스
│   │   ├── GeneratorRegistry.java         # 생성기 등록·조회
│   │   ├── EntityGenerator.java           # 엔티티 단위 N건 생성
│   │   ├── StringValueGenerator.java
│   │   ├── IntegerValueGenerator.java
│   │   ├── LongValueGenerator.java
│   │   ├── DoubleValueGenerator.java
│   │   ├── BooleanValueGenerator.java
│   │   ├── LocalDateValueGenerator.java
│   │   ├── LocalDateTimeValueGenerator.java
│   │   └── domain/
│   │       ├── UuidValueGenerator.java
│   │       ├── KoreanNameValueGenerator.java
│   │       ├── EmailValueGenerator.java
│   │       ├── PhoneValueGenerator.java
│   │       ├── AddressValueGenerator.java
│   │       └── NumericCodeValueGenerator.java
│   ├── model/
│   │   ├── FieldType.java                 # 지원 타입 enum
│   │   ├── FieldDefinition.java           # 필드명 + 타입 + 옵션
│   │   └── DataSet.java                   # 레이블 + rows
│   ├── store/
│   │   └── InMemoryDataStore.java         # 메모리 데이터 저장소
│   └── util/
│       ├── DataPrinter.java               # 콘솔 테이블 출력
│       └── DataValidator.java             # 건수·필드 검증
└── test/java/org/example/                 # 단위 테스트 (총 70개)
```

## 지원 타입

### 기본 타입

| FieldType | 생성 값 | 주요 옵션 |
|-----------|---------|-----------|
| `STRING` | 랜덤 영숫자 문자열 | `length` (기본 8) |
| `INTEGER` | 랜덤 정수 | `min`, `max` |
| `LONG` | 랜덤 Long | `min`, `max` |
| `DOUBLE` | 랜덤 실수 | `min`, `max`, `scale` (소수점 자리, 기본 2) |
| `BOOLEAN` | `true` / `false` | — |
| `LOCAL_DATE` | 랜덤 날짜 | `from`, `to` (ISO 날짜 문자열) |
| `LOCAL_DATETIME` | 랜덤 일시 | `from`, `to` (ISO 일시 문자열) |

### 도메인 특화 타입

| FieldType | 생성 값 | 주요 옵션 |
|-----------|---------|-----------|
| `UUID` | 표준 UUID v4 | — |
| `KOREAN_NAME` | 한국어 성+이름 (3글자) | — |
| `EMAIL` | `{영숫자8자}@{도메인}` | — |
| `PHONE` | `010-XXXX-XXXX` | — |
| `ADDRESS` | 시 + 구 + 동 조합 | — |
| `NUMERIC_CODE` | zero-padded 숫자 문자열 | `digits` (기본 6) |

## 사용법

### 1. 필드 정의

```java
List<FieldDefinition> fields = List.of(
    FieldDefinition.of("id",       FieldType.UUID),
    FieldDefinition.of("name",     FieldType.KOREAN_NAME),
    FieldDefinition.of("email",    FieldType.EMAIL),
    FieldDefinition.of("age",      FieldType.INTEGER, Map.of("min", 20, "max", 60)),
    FieldDefinition.of("joinDate", FieldType.LOCAL_DATE,
            Map.of("from", "2020-01-01", "to", "2024-12-31"))
);
```

### 2. 데이터 생성

```java
GeneratorRegistry registry = new GeneratorRegistry();
EntityGenerator generator = new EntityGenerator(fields, registry);

List<Map<String, Object>> rows = generator.generate(1000);
```

### 3. 메모리 저장 및 조회

```java
InMemoryDataStore store = new InMemoryDataStore();
store.save("users", rows);

List<Map<String, Object>> saved = store.get("users");  // 조회
store.append("users", moreRows);                        // 추가
store.remove("users");                                  // 삭제
store.clear();                                          // 전체 삭제
```

### 4. 검증 및 출력

```java
DataValidator validator = new DataValidator();
validator.validateCount(rows, 1000);
validator.validateRequiredFields(rows, List.of("id", "name", "email"));

DataPrinter printer = new DataPrinter();
printer.print(rows);
```

출력 예시 (100건 이하는 전체, 초과 시 앞 10건 + 총 건수 표시):

```
┌──────────┬──────────────────────────────────────┬────────┬──────────────────────┬─────┐
│ joinDate │ id                                   │ name   │ email                │ age │
├──────────┼──────────────────────────────────────┼────────┼──────────────────────┼─────┤
│ 2022-03-15 │ 8f4aae49-9ed1-4715-99b0-cf920cddbe9b │ 류유원 │ o28qwl3i@outlook.com │ 46  │
│ 2019-11-25 │ 15cb9485-0afb-4362-98fb-55078805c6eb │ 임준민 │ kwypgysd@kakao.com   │ 26  │
└──────────┴──────────────────────────────────────┴────────┴──────────────────────┴─────┘
... (총 1000건, 앞 10건만 표시)
```

### 5. 커스텀 생성기 등록

`ValueGenerator<T>` 인터페이스를 구현하면 기존 코드 수정 없이 새 타입을 추가할 수 있습니다.

```java
registry.register(FieldType.STRING, options -> "고정값");
```

## 실행

```bash
# 빌드 및 Main 시나리오 실행
./gradlew run

# 전체 테스트 실행
./gradlew test
```

> Windows 환경에서 한글 출력이 필요한 경우 아래 명령어를 사용합니다.
> ```bash
> ./gradlew classes && java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp "build/classes/java/main" org.example.Main
> ```

## 성능

| 생성 건수 | 소요 시간 |
|-----------|-----------|
| 1,000건 | ~5ms |
| 10,000건 | ~30ms |
| 100,000건 | ~400ms |

## 테스트

단위 테스트 70개, 모든 생성기·저장소·유틸리티 클래스를 커버합니다.

```bash
./gradlew test
```

## 범위 외 항목

- DB 연결 및 데이터 입력 (별도 PoC에서 수행)
- 파일(CSV, JSON) 내보내기
- REST API 제공
