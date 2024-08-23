# 만들면서 배우는 스프링
[Next Step - 과정 소개](https://edu.nextstep.camp/c/4YUvqn9V)

## DI 컨테이너 구현하기

### 학습목표
- DI 컨테이너 구현을 통해 내부 동작 원리를 이해한다.
- IoC와 DI의 이해도를 높인다.

### 시작 가이드
1. 이전 미션에서 진행한 코드를 사용하고 싶다면, 마이그레이션 작업을 진행합니다.
2. 학습 테스트는 강의 시간에 풀어봅시다.
3. LMS의 1단계 미션부터 진행합니다.

### 준비 사항
- IntelliJ에 Kotest 플러그인 설치

## 학습 테스트
- 스프링 IoC 컨테이너에 대해 좀 더 자세히 알아봅시다.
- 실패하는 학습 테스트를 통과시키시면 됩니다.
- 학습 테스트는 ioc 패키지 또는 클래스 단위로 실행하세요.

1. [스프링 IoC 컨테이너와 Bean 소개](study/src/test/kotlin/ioc/Introduction.kt)
2. [컨테이너 개요](study/src/test/kotlin/ioc/Container.kt)
3. [Bean 개요](study/src/test/kotlin/ioc/Bean.kt)
4. [의존성(Dependencies)](study/src/test/kotlin/ioc/Dependencies.kt)
5. [Bean 스코프](study/src/test/kotlin/ioc/BeanScopes.kt)
6. [Bean의 라이프 사이클](study/src/test/kotlin/ioc/Lifecycle.kt)
7. [어노테이션 기반 컨테이너 구성](study/src/test/kotlin/ioc/AnnotationBasedConfiguration.kt)
8. [자바 기반 컨테이 구성](study/src/test/kotlin/ioc/JavaBasedConfiguration.kt)

### 싱글톤 스코프
<img src="docs/images/singleton.png" alt="singleton">

### 프로토타입 스코프
<img src="docs/images/prototype.png" alt="prototype">

## 미션
### 1단계 미션 요구사항
- [x] `@Autowired` 어노테이션을 사용하여 의존성 주입을 구현한다.

### 2단계 미션 요구사항
- [x] AnnotationHandlerMapping이 BeanFactory와 BeanScanner를 활용해 동작하도록 리팩터링한다.

### 3단계 미션 요구사항
- 추후에 데이터베이스에 연결할 수 있도록 `javax.sql.DataSource`로 데이터베이스 설정 정보를 관리
- `@Configuration` 기능을 추가하여 빈 인스턴스로 관리할 수 있도록 한다.
    - 각 메소드에서 생성하는 인스턴스가 `BeanFactory`에 빈으로 등록하라는 설정은 `@Bean`으로 한다.
    - `BeanScanner`에서 사용할 기본 패키지에 대한 설정을 하드코딩했는데 `@ComponentScan`으로 패키지 경로를 설정할 수 있도록 지원한다
    - `@Configuration` 설정 파일을 통해 등록한 빈과 `BeanScanner`를 통해 등록한 빈 간에도 DI가 가능해야 한다.

### 4단계 미션 요구사항
- [ ] 설정 파일을 읽어 Bean 등록 기능 추가
- [ ] 설정 파일을 통해 등록한 Bean과 ClasspathBeanScanner를 통해 추가한 Bean 통합
- [ ] ConfigurationBeanScanner와 ClasspathBeanScanner을 통합하는 클래스 추가