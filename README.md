<!-- TOC -->
* [Mysql Docker로 띄우기](#mysql-docker로-띄우기)
* [@EnableBatchProcessing](#enablebatchprocessing)
* [BatchAutoConfiguration.java](#batchautoconfigurationjava)
* [DB 스키마](#db-스키마)
  * [스키마 DDL sql 파일 위치](#스키마-ddl-sql-파일-위치-)
  * [스키마 생성 설정](#스키마-생성-설정)
* [DB Table](#db-table)
* [Multiple Job Support X](#multiple-job-support-x)
* [Job](#job)
  * [기본 구현체](#기본-구현체)
<!-- TOC -->
# Mysql Docker로 띄우기
```
docker run --name mysql -e MYSQL_ROOT_PASSWORD=password -d -p 3306:3306 mysql:latest
```

# @EnableBatchProcessing

# BatchAutoConfiguration.java
```
@ConditionalOnMissingBean(value = DefaultBatchConfiguration.class, annotation = EnableBatchProcessing.class)
해당 빈들이 존재하지 않을때 batchAutoConfiguration bean을 생성한다.
   
JobLauncherApplicationRunner 이 이안에 들어있어서, 스프링어플리케이션을 돌려도 자동으로 실행되지 않는다.
자세히 살펴보면  
@ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
> "spring.batch.job.enabled" 속성이 "true"로 설정되거나 정의되지 않으면 Bean 생성이 진행되어 배치 처리가 활성화된다
```

# DB 스키마
## 스키마 DDL sql 파일 위치 
org.springframework.batch.core.schema-*.sql
## 스키마 생성 설정
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

# DB Table
![Spring Batch Meta-Data ERD.png](doc%2Fpic%2FSpring%20Batch%20Meta-Data%20ERD.png)
# Multiple Job Support X
참조 
- 스프링배치 5.0 변경사항 https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#multiple-batch-jobs
- 멀티잡 구현 https://velog.io/@hoyo1744/SpringBatch%EC%97%90%EC%84%9C-MultiJob-%EC%88%98%ED%96%89%ED%95%98%EA%B8%B0

이전 버전들과 다르게 여러개의 job이 빈등록 되어 있을때 에러가 발생한다.
```yaml
spring:
  batch:
    job:
      name: helloJob
      enabled: true
```
> spring.batch.job.name 으로 여러개의 job 중 하나를 지정하여 수행할 수 있다.
> > springbatch5 이전에 names: job1,job2 와 같은 문법이 수행되었지만, 지원안함

> spring.batch.job.enabled: false 로 줘서 어플리케이션 기동시 모든 job을 수행하지 않을수도 있다.

# Job
> 1. 배치 계층 구조에서 가장 상위에 있는 개념   
> 2. 하나의 배치작업 자체를 의미한다  
> 3. Job Configuration을 통해 생성되는 객체단위로 배치작업을 전체적으로 설정하고 명세해 놓은 객체  
> 4. 배치 Job을 구성하기 위한 최상위 인터페이스이며 스프링 배치가 기본 구현체 제공  
> 5. 반드시 하나 이상의 Step으로 구성해야함

## 기본 구현체
1. SimpleJob
   - 순차적으로 Step 실행
2. FlowJob
   - 특정 조건으로 Step 구성하여 실행
   - Flow 객체를 실행시켜서 작업 진행

# JobInstance
> 1. Job이 실행될때 생성되는 Job의 논리적 실행단위 객체
> 2. 고유하게 식별 가능
> 3. 테이블에 담기 위한 메타데이터 도메인이라고 생각하면 된다.
> 4. Job의 설정과 구성은 동일하지만 Job이 실행되는 시점에 처리하는 내용은 다르기 때문에 Job의 실행을 구분해야 한다.
>> - 하루에 한번식 배치 Job이 실행된다면 매일 실행되는 각각의 Job을 JobInstance로 표현한다.
> 5. JobInstance 생성 및 실행
>> - 처음 시작하는 Job + JobParameter(실제 Job을 실행시키는 JobLauncher의 파라미터)일 경우 JobInstance 생성
>> - 이전과 동일한 Job + JobParameter 실행시 이미 존재하는 JobInstance 리턴
>>> - 내부적으로 JobName + JobKey(JobParameter 해시값)을 가지고 JobInstance 객체를 얻음
> 6. Job:JobInstance = 1:N

## BATCH_JOB_INSTANCE 테이블 매핑
- JOB_NAME(Job)과 JOB_KEY(JobParameter의 해시값)가 동일한 데이터는 중복 저장 불가
  - 매번 파라미터가 다르게해서 Job을 돌려야 한다.

## TEST
동일 JOB_NAME, JOB_KEY를 사용할때와, 서로다른 JOB_NAME, JOB_KEY를 사용할때를 보기위해서 
JobLauncher를 직접 구성해서 TEST 해본다.
결과는 BATCH_JOB_EXECUTION_PARAMS, BATCH_JOB_INSTANCE 확인한다.