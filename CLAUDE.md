# DummyDataGenerator — CLAUDE.md

## 1. 프로젝트 개요

DB 성능 테스트·통합 테스트·개발 환경 데이터 세팅에 사용할 더미 데이터를 프로그래밍 방식으로 생성하는 Java PoC 프로젝트다.

- 생성된 데이터는 **JVM 메모리에만 보관**한다 (DB 입력은 별도 PoC에서 수행).
- 상세 기능 요구사항은 `PRD.md`, 구현 계획은 `PLAN.md`를 참조한다.

### 기술 스택

| 항목 | 내용 |
|------|------|
| Language | Java 17 |
| Build | Gradle 8 (Kotlin DSL) |
| Test | JUnit Jupiter 6 |
| 외부 라이브러리 | 없음 (Java 표준 라이브러리만 사용) |

---

## 2. 패키지 구조

```
src/main/java/org/example/
├── Main.java                          ← 실행 시나리오
├── generator/
│   ├── ValueGenerator.java            (interface)
│   ├── GeneratorRegistry.java
│   ├── EntityGenerator.java
│   ├── StringValueGenerator.java
│   ├── IntegerValueGenerator.java
│   ├── LongValueGenerator.java
│   ├── DoubleValueGenerator.java
│   ├── BooleanValueGenerator.java
│   ├── LocalDateValueGenerator.java
│   ├── LocalDateTimeValueGenerator.java
│   └── domain/
│       ├── UuidValueGenerator.java
│       ├── KoreanNameValueGenerator.java
│       ├── EmailValueGenerator.java
│       ├── PhoneValueGenerator.java
│       ├── AddressValueGenerator.java
│       └── NumericCodeValueGenerator.java
├── model/
│   ├── FieldType.java                 (enum)
│   ├── FieldDefinition.java
│   └── DataSet.java
├── store/
│   └── InMemoryDataStore.java
└── util/
    ├── DataPrinter.java
    └── DataValidator.java
```

---

## 3. 개발 프로세스 (TDD)

이 프로젝트는 **TDD(Test-Driven Development)** 방식으로 개발한다.  
각 Phase는 아래 흐름을 반드시 따른다.

```
RED  →  사용자 승인  →  GREEN  →  사용자 검증  →  커밋 & 푸쉬
```

### 3-1. RED 단계

1. 구현 대상 클래스의 테스트 코드를 먼저 작성한다.
2. **작성 완료 후 사용자에게 테스트 코드 검토를 요청**한다.
3. 사용자의 승인이 확인되기 전까지 GREEN 구현을 시작하지 않는다.

### 3-2. GREEN 단계

1. 승인된 테스트를 통과시키는 최소한의 구현 코드를 작성한다.
2. 구현 완료 후 사용자에게 검증을 요청한다.
3. **사용자가 검증 통과를 확인하면** 해당 Phase를 커밋 & 푸쉬한다.

### 3-3. 커밋 & 푸쉬 규칙

- 사용자의 명시적인 통과 확인 없이 커밋·푸쉬를 진행하지 않는다.
- 커밋 메시지는 아래 형식을 따른다.

```
[Phase N] <작업 내용 한 줄 요약>

예:
[Phase 1] 모델 클래스 정의 (FieldType, FieldDefinition, DataSet)
[Phase 2] 기본 타입 ValueGenerator 7종 구현
```

---

## 4. 구현 Phase 요약

| Phase | 주요 작업 | 핵심 파일 |
|-------|-----------|-----------|
| 1 | 모델 정의 | `FieldType`, `FieldDefinition`, `DataSet` |
| 2 | 기본 타입 생성기 | `ValueGenerator` + 7종 구현체 |
| 3 | 도메인 특화 생성기 | `domain/` 하위 6종 |
| 4 | 엔티티 생성기 & 저장소 | `EntityGenerator`, `GeneratorRegistry`, `InMemoryDataStore` |
| 5 | 출력·검증 유틸리티 | `DataPrinter`, `DataValidator` |
| 6 | Main 시나리오 & 마무리 | `Main.java` 재작성, DoD 점검 |

각 Phase의 상세 내용은 `PLAN.md`를 참조한다.

---

## 5. 코딩 규칙

- 외부 라이브러리를 추가하지 않는다. `java.util.Random`, `java.time.*` 등 표준 라이브러리로 구현한다.
- 생성기 확장은 `ValueGenerator<T>` 구현체를 추가하고 `GeneratorRegistry`에 등록하는 방식으로 처리한다. 기존 클래스를 수정하지 않는다.
- 검증 실패 시 `IllegalStateException`을 던진다 (PoC 수준).
- 주석은 WHY가 불명확한 경우에만 작성한다. 코드가 말하는 내용은 주석으로 반복하지 않는다.

---

## 6. 완료 기준 (DoD)

- [ ] 기본 타입 7종 생성기 구현 및 단위 테스트 통과
- [ ] 도메인 특화 6종 생성기 구현 및 단위 테스트 통과
- [ ] `EntityGenerator` N건 일괄 생성 동작 확인
- [ ] `InMemoryDataStore` 저장·조회·삭제 정상 동작
- [ ] `Main` 두 시나리오(사용자 1,000건 / 주문 500건) 실행 성공
- [ ] 10만 건 생성 10초 이내 완료
- [ ] 단위 테스트 커버리지 80% 이상
