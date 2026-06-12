# PRD: DummyDataGenerator

## 1. 개요

### 1.1 프로젝트 목적
DB 성능 테스트, 통합 테스트, 개발 환경 데이터 세팅 등에 활용하기 위한 더미 데이터를 프로그래밍 방식으로 생성하는 PoC 프로젝트다.

### 1.2 범위
- **포함**: 다양한 타입의 더미 데이터 생성 및 메모리 보관
- **제외**: DB 연결 및 데이터 입력 (별도 PoC에서 수행)

### 1.3 기술 스택
- Language: Java 17+
- Build: Gradle (Kotlin DSL)
- Test: JUnit Jupiter 6

---

## 2. 기능 요구사항

### 2.1 데이터 생성

#### 2.1.1 기본 타입 생성
| 타입 | 설명 | 예시 |
|------|------|------|
| String | 랜덤 문자열 (길이 지정 가능) | `"aBcDeFgH"` |
| Integer | 범위 내 랜덤 정수 | `42` |
| Long | 범위 내 랜덤 Long | `1234567890L` |
| Double | 범위 내 랜덤 실수 | `3.14` |
| Boolean | 랜덤 true/false | `true` |
| LocalDate | 범위 내 랜덤 날짜 | `2024-03-15` |
| LocalDateTime | 범위 내 랜덤 일시 | `2024-03-15T10:30:00` |

#### 2.1.2 도메인 특화 데이터 생성
| 항목 | 설명 |
|------|------|
| 이름 | 한국어 성+이름 조합 |
| 이메일 | `{랜덤문자열}@{도메인}` 형식 |
| 전화번호 | `010-XXXX-XXXX` 형식 |
| 주소 | 시/구/동 조합 |
| UUID | 표준 UUID v4 |
| 숫자 코드 | 지정 자릿수의 zero-padded 숫자 문자열 |

#### 2.1.3 엔티티 단위 생성
- 사용자 정의 필드 구성(이름, 타입, 옵션)을 받아 Map<String, Object> 형태의 레코드를 생성한다.
- N건 일괄 생성 시 List<Map<String, Object>> 형태로 반환한다.

### 2.2 데이터 보관 (메모리)
- 생성된 데이터는 JVM 힙 메모리에 List 형태로 보관한다.
- 데이터셋에 이름(레이블)을 부여해 복수의 데이터셋을 구분하여 관리한다.
- 보관된 데이터는 조회, 추가, 전체 삭제가 가능하다.
- DB 저장 기능은 구현하지 않는다.

### 2.3 출력 및 검증
- 생성된 데이터를 콘솔(stdout)에 테이블 형식으로 출력하는 기능을 제공한다.
- 지정한 건수만큼 생성되었는지 검증한다.
- 필수 필드 누락 여부를 검증한다.

---

## 3. 비기능 요구사항

| 항목 | 요구사항 |
|------|----------|
| 성능 | 10만 건 생성 시 10초 이내 완료 |
| 확장성 | 새로운 도메인 타입을 추가할 때 기존 코드 수정 최소화 (전략 패턴 권장) |
| 테스트 | 각 생성기 단위 테스트 커버리지 80% 이상 |
| 의존성 | 외부 라이브러리 최소화 (Java 표준 라이브러리 우선) |

---

## 4. 설계 방향

### 4.1 핵심 컴포넌트

```
DummyDataGenerator
├── generator/
│   ├── DataGenerator (interface)
│   ├── StringGenerator
│   ├── NumberGenerator
│   ├── DateGenerator
│   └── DomainDataGenerator   ← 이름, 이메일, 전화번호 등
├── model/
│   ├── FieldDefinition        ← 필드명 + 타입 + 생성 옵션
│   └── DataSet                ← 레이블 + List<Map<String,Object>>
├── store/
│   └── InMemoryDataStore      ← 생성된 데이터셋 보관
└── Main                       ← 실행 예시 및 시나리오
```

### 4.2 사용 흐름 (예시)

```java
// 1. 필드 정의
List<FieldDefinition> fields = List.of(
    FieldDefinition.of("id",    FieldType.UUID),
    FieldDefinition.of("name",  FieldType.KOREAN_NAME),
    FieldDefinition.of("email", FieldType.EMAIL),
    FieldDefinition.of("age",   FieldType.INTEGER, Map.of("min", 20, "max", 60))
);

// 2. 데이터 생성
DummyDataGenerator generator = new DummyDataGenerator(fields);
List<Map<String, Object>> rows = generator.generate(1000);

// 3. 메모리 보관
InMemoryDataStore store = new InMemoryDataStore();
store.save("users", rows);

// 4. 조회 및 출력
store.get("users").forEach(System.out::println);
```

---

## 5. 완료 기준 (DoD)

- [ ] 기본 타입 7종 생성기 구현 및 단위 테스트 통과
- [ ] 도메인 특화 데이터 5종(이름·이메일·전화번호·주소·UUID) 생성기 구현 및 단위 테스트 통과
- [ ] 엔티티 단위 N건 일괄 생성 기능 구현
- [ ] InMemoryDataStore 저장·조회·삭제 기능 구현
- [ ] Main에서 1,000건 생성 → 저장 → 조회 시나리오 실행 가능
- [ ] 전체 단위 테스트 커버리지 80% 이상

---

## 6. 제외 항목 (Out of Scope)

- JDBC / JPA 연동
- 파일(CSV, JSON) 내보내기
- REST API 제공
- 멀티스레드 병렬 생성 (단일 스레드로 충분한 성능 확인 후 결정)
