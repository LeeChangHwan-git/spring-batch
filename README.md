# Spring-batch Inflearn

## Mysql Docker로 띄우기
```
docker run --name mysql -e MYSQL_ROOT_PASSWORD=password -d -p 3306:3306 mysql:latest
```

## @EnableBatchProcessing

## BatchAutoConfiguration.java
```
@ConditionalOnMissingBean(value = DefaultBatchConfiguration.class, annotation = EnableBatchProcessing.class)
해당 빈들이 존재하지 않을때 batchAutoConfiguration bean을 생성한다.
   
JobLauncherApplicationRunner 이 이안에 들어있어서, 스프링어플리케이션을 돌려도 자동으로 실행되지 않는다.
자세히 살펴보면  
@ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
> "spring.batch.job.enabled" 속성이 "true"로 설정되거나 정의되지 않으면 Bean 생성이 진행되어 배치 처리가 활성화된다
```

## DB 스키마
### 스키마 DDL sql 파일 위치 
org.springframework.batch.core.schema-*.sql
### 스키마 생성 설정
수동: 쿼리복사 후 직접 실행   
자동 : spring.batch.jdbc.initialize-schema 설정
- ALWAYS
  - 스크립트 항상 실행
  - 우선순위: RDBMS > 내장 DB
- EMBEDDED
  - 내장 DB일때만 실행되며 스키마가 자동생성됨.
- NEVER
  - 스크립트 항상 실행안됨
  - 메타테이블을 따로 생성해야한다.

## DB Table

