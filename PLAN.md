# PLAN: DummyDataGenerator 구현 계획

## 구현 순서 개요

```
Phase 1: 모델 정의
Phase 2: 기본 타입 생성기
Phase 3: 도메인 특화 생성기
Phase 4: 엔티티 단위 생성기 & InMemoryDataStore
Phase 5: 출력·검증 유틸리티
Phase 6: Main 시나리오 & 마무리
```

---

## Phase 1: 모델 정의

> 다른 모든 컴포넌트가 참조하는 타입을 먼저 확정한다.

### 1-1. `FieldType` (enum)

```
src/main/java/org/example/model/FieldType.java
```

- 생성기가 지원하는 모든 타입을 열거값으로 선언한다.

```
STRING, INTEGER, LONG, DOUBLE, BOOLEAN,
LOCAL_DATE, LOCAL_DATETIME,
UUID, KOREAN_NAME, EMAIL, PHONE, ADDRESS, NUMERIC_CODE
```

### 1-2. `FieldDefinition` (record / class)

```
src/main/java/org/example/model/FieldDefinition.java
```

- 필드명, `FieldType`, 옵션(`Map<String, Object>`)을 보유한다.
- 정적 팩토리 메서드를 제공한다.

```java
FieldDefinition.of("age", FieldType.INTEGER, Map.of("min", 20, "max", 60))
FieldDefinition.of("id",  FieldType.UUID)   // 옵션 없는 단축 형태
```

### 1-3. `DataSet` (class)

```
src/main/java/org/example/model/DataSet.java
```

- 레이블(String)과 `List<Map<String, Object>>`를 보유한다.
- `size()` 편의 메서드를 제공한다.

**테스트**
- `FieldDefinition` 생성 및 옵션 조회 정상 동작 확인
- `DataSet` size() 반환값 확인

---

## Phase 2: 기본 타입 생성기

> 각 생성기는 독립적으로 구현·테스트하고, 공통 인터페이스를 통해 교체 가능하게 한다.

### 2-1. `ValueGenerator<T>` (interface)

```
src/main/java/org/example/generator/ValueGenerator.java
```

```java
public interface ValueGenerator<T> {
    T generate(Map<String, Object> options);
}
```

### 2-2. 기본 타입 생성기 구현체 (7종)

```
src/main/java/org/example/generator/
    StringValueGenerator.java     // 옵션: length(기본 8)
    IntegerValueGenerator.java    // 옵션: min, max
    LongValueGenerator.java       // 옵션: min, max
    DoubleValueGenerator.java     // 옵션: min, max, scale(소수점 자리)
    BooleanValueGenerator.java    // 옵션 없음
    LocalDateValueGenerator.java  // 옵션: from, to (ISO 문자열)
    LocalDateTimeValueGenerator.java // 옵션: from, to (ISO 문자열)
```

- `java.util.Random` 사용 (외부 라이브러리 없음)
- 옵션 누락 시 합리적인 기본값 적용

**테스트** (`src/test/java/org/example/generator/`)
- 각 생성기별 테스트 클래스
- 범위 경계값, 기본값, null 옵션 케이스 포함

---

## Phase 3: 도메인 특화 생성기

> 실제 데이터처럼 보이는 값을 생성한다. 모두 `ValueGenerator<String>` 구현.

### 3-1. 구현체 목록 (6종)

```
src/main/java/org/example/generator/domain/
    UuidValueGenerator.java          // UUID.randomUUID()
    KoreanNameValueGenerator.java    // 성(25개) + 이름(2글자 랜덤 조합)
    EmailValueGenerator.java         // {8자 영소문자}@{도메인 풀}
    PhoneValueGenerator.java         // 010-{4자리}-{4자리}
    AddressValueGenerator.java       // 시 + 구 + 동 랜덤 조합
    NumericCodeValueGenerator.java   // 옵션: digits(기본 6), zero-padded
```

**한국어 데이터 풀** (하드코딩, 별도 파일 없이 상수로 관리)
- 성: 김·이·박·최·정 등 25개
- 이름 음절: 60개 내외 조합용 글자 풀
- 도메인: gmail.com, naver.com, kakao.com 등 5개
- 시/구/동: 서울 주요 행정구역 기반 샘플 풀

**테스트**
- 형식 정규식 검증 (이메일 `@` 포함, 전화번호 `010-` 시작 등)
- 반복 호출 시 다양한 값 생성 확인 (동일값 연속 5회 이하)

---

## Phase 4: 엔티티 생성기 & InMemoryDataStore

### 4-1. `GeneratorRegistry` (class)

```
src/main/java/org/example/generator/GeneratorRegistry.java
```

- `FieldType` → `ValueGenerator` 매핑을 보관한다.
- 기본 생성기가 사전 등록된 상태로 초기화된다.
- 커스텀 생성기를 등록할 수 있다 (`register(FieldType, ValueGenerator)`).

### 4-2. `EntityGenerator` (class)

```
src/main/java/org/example/generator/EntityGenerator.java
```

- `List<FieldDefinition>`과 `GeneratorRegistry`를 받아 초기화한다.
- `generate(int count)` → `List<Map<String, Object>>`
- 각 레코드: `{ "fieldName": generatedValue, ... }`

```java
EntityGenerator gen = new EntityGenerator(fields, registry);
List<Map<String, Object>> rows = gen.generate(1000);
```

### 4-3. `InMemoryDataStore` (class)

```
src/main/java/org/example/store/InMemoryDataStore.java
```

| 메서드 | 설명 |
|--------|------|
| `save(String label, List<...> rows)` | 데이터셋 저장 (동일 레이블 덮어씀) |
| `append(String label, List<...> rows)` | 기존 데이터셋에 추가 |
| `get(String label)` | 데이터셋 조회, 없으면 빈 리스트 반환 |
| `labels()` | 저장된 레이블 목록 반환 |
| `remove(String label)` | 특정 데이터셋 삭제 |
| `clear()` | 전체 초기화 |
| `size(String label)` | 특정 데이터셋 건수 반환 |

**테스트**
- `EntityGenerator` : 지정 건수 생성, 모든 필드 존재, 타입 일치
- `InMemoryDataStore` : 저장·조회·추가·삭제·전체삭제

---

## Phase 5: 출력·검증 유틸리티

### 5-1. `DataPrinter` (class)

```
src/main/java/org/example/util/DataPrinter.java
```

- `print(List<Map<String,Object>> rows)` : 헤더 + 데이터를 콘솔 테이블로 출력
- 컬럼 너비는 최대 값 길이에 맞춰 자동 조정
- 출력 예시:

```
┌──────────────────────────┬──────────┬───────────────────────────┐
│ id                       │ name     │ email                     │
├──────────────────────────┼──────────┼───────────────────────────┤
│ a1b2c3d4-...             │ 김민준   │ xkqrabcd@naver.com        │
│ e5f6g7h8-...             │ 이서윤   │ zypqefgh@gmail.com        │
└──────────────────────────┴──────────┴───────────────────────────┘
```

- 100건 초과 시 앞 10건만 출력하고 `... (총 N건)` 메시지를 표시한다.

### 5-2. `DataValidator` (class)

```
src/main/java/org/example/util/DataValidator.java
```

| 메서드 | 설명 |
|--------|------|
| `validateCount(List<?> rows, int expected)` | 건수 불일치 시 예외 |
| `validateRequiredFields(List<Map<String,Object>> rows, List<String> fields)` | 누락 필드 존재 시 예외 |

- 검증 실패 시 `IllegalStateException`을 던진다 (PoC 수준으로 충분).

**테스트**
- `DataValidator` : 정상 케이스, 건수 불일치, 필드 누락 각각 검증

---

## Phase 6: Main 시나리오 & 마무리

### 6-1. `Main.java` 시나리오 재작성

두 가지 시나리오를 순서대로 실행한다.

**시나리오 A: 사용자 테이블 1,000건**
```
필드: id(UUID), name(KOREAN_NAME), email(EMAIL),
      phone(PHONE), age(INTEGER 20~60), joinDate(LOCAL_DATE)
```

**시나리오 B: 주문 테이블 500건**
```
필드: orderId(NUMERIC_CODE digits=8), userId(UUID),
      amount(DOUBLE 1000~500000), orderDate(LOCAL_DATETIME),
      isPaid(BOOLEAN), address(ADDRESS)
```

각 시나리오에서 생성 → 검증 → InMemoryDataStore 저장 → DataPrinter 출력까지 수행한다.

### 6-2. `build.gradle.kts` 보완

- Java 17 소스 호환성 명시
- 필요 시 JaCoCo 플러그인 추가 (커버리지 리포트)

```kotlin
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
```

### 6-3. DoD 최종 점검

- [ ] 기본 타입 7종 생성기 구현 및 단위 테스트 통과
- [ ] 도메인 특화 6종 생성기 구현 및 단위 테스트 통과
- [ ] `EntityGenerator` N건 일괄 생성 동작 확인
- [ ] `InMemoryDataStore` 저장·조회·삭제 정상 동작
- [ ] `Main` 두 시나리오 실행 성공
- [ ] 10만 건 생성 10초 이내 완료 (로컬 실행으로 확인)
- [ ] 단위 테스트 커버리지 80% 이상

---

## 파일 구조 최종 목표

```
src/
├── main/java/org/example/
│   ├── Main.java
│   ├── generator/
│   │   ├── ValueGenerator.java          (interface)
│   │   ├── GeneratorRegistry.java
│   │   ├── EntityGenerator.java
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
│   │   ├── FieldType.java
│   │   ├── FieldDefinition.java
│   │   └── DataSet.java
│   ├── store/
│   │   └── InMemoryDataStore.java
│   └── util/
│       ├── DataPrinter.java
│       └── DataValidator.java
└── test/java/org/example/
    ├── generator/
    │   ├── StringValueGeneratorTest.java
    │   ├── IntegerValueGeneratorTest.java
    │   ├── LongValueGeneratorTest.java
    │   ├── DoubleValueGeneratorTest.java
    │   ├── BooleanValueGeneratorTest.java
    │   ├── LocalDateValueGeneratorTest.java
    │   ├── LocalDateTimeValueGeneratorTest.java
    │   ├── EntityGeneratorTest.java
    │   └── domain/
    │       ├── UuidValueGeneratorTest.java
    │       ├── KoreanNameValueGeneratorTest.java
    │       ├── EmailValueGeneratorTest.java
    │       ├── PhoneValueGeneratorTest.java
    │       ├── AddressValueGeneratorTest.java
    │       └── NumericCodeValueGeneratorTest.java
    ├── model/
    │   └── FieldDefinitionTest.java
    ├── store/
    │   └── InMemoryDataStoreTest.java
    └── util/
        └── DataValidatorTest.java
```
