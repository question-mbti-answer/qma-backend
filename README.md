#QmA

## 설명

서로의 MBTI에 대한 이해를 넓히고, 사람들이 서로를 이해하고 소통하는 데 도움이 되는 플랫폼을 제공하는 것을 목표로 합니다.

## 아키텍처 구조

![qma아키텍처](https://github.com/question-mbti-answer/qma-backend/assets/61047602/fa3fc55a-c6e7-4a91-865e-fc4c048fa366)

## ERD 다이어그램

![QmA (1)](https://github.com/question-mbti-answer/qma-backend/assets/61047602/577af59f-ecb6-4cb8-bea2-a8f2aacf598f)

## 기술 선택 과정

### QueryDsl

### Caffeine Cache

### Redis

## 트러블 슈팅 기록

### 테스트 환경에서 JpaAudit이 적용 안되는 경우

```java
@Configuration  
@EnableJpaAuditing  
public class JpaConfig {  
}
```

Configuration 빈을 등록해주고

테스트 환경에서

```java
@Import(JpaConfig.class)  
class test{
//...
}
```
Import를 통해 Audit이 필요한 테스트에서 활용하면 해결된다.

### 배포 환경에서 API 명세서 빌드가 안되는 경우

resources 경로에 static/docs 경로가 존재해야 open api 명세서가 생성되는 경우였다.
Repository 에 Push 할 때에는 docs 디렉토리에 명세서가 존재하지 않고 배포 환경에서 build를 통해 생성되도록 해뒀었다.
하지만 Repository 에 올라갈 때 해당 경로에 파일이 존재하지 않을 경우 디렉토리가 존재하지 않게 되어 원하는 대로 명세서가 생성되지 않는 경우가 발생했다.
해당 문제를 해결하기 위해 docs 경로에 .keep 파일을 생성하여 디렉토리가 유지되도록 설정하여 해결하였다.

### QueryParam String이 null로 안받아지는 경우

```text
http://server.com?cond1=&cond2=
```
해당 방식을 통해 query param을 입력받을 경우 cond1 과 cond2는 null로 처리되는줄 알았다.
하지만 실제 입력에서는 "" 과 같은 비어있는 문자열로 입력되어 해당 이슈가 발생한 검색 기능에서 동적 쿼리가 원하는 대로 동작하지 않는 경우가 발생했다.
원하는 대로 동작하려면

```text
http://server.com
```
위와 같이 query param 내용 자체가 비어있는 상태로 전달이 되어야하는데 이는 요청 자체를 신뢰해야 하는 상황이 발생된다.

```java
@Component
public class StringToNullConverter implements Converter<String, String> {
    @Override
    public String convert(String source) {
        return source.isEmpty() ? null : source;
    }
}
```

String이 비어있는 "" 값이면 null로 변환하는 Converter를 구현해주고

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final StringToNullConverter stringToNullConverter;

    public WebConfig(StringToNullConverter stringToNullConverter) {
        this.stringToNullConverter = stringToNullConverter;
    }

    // ...
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToNullConverter);
    }
}
```

WebMvcConfigurer 구현체에 등록해서 해결하였다.


## 학습 내용