<!-- TOC -->
* [출처](#출처)
* [SpringBatch 환경준비](#springbatch-환경준비)
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
* [JobInstance](#jobinstance)
  * [BATCH_JOB_INSTANCE 테이블 매핑](#batchjobinstance-테이블-매핑)
  * [TEST](#test)
* [JobParameter](#jobparameter)
  * [생성 및 바인딩](#생성-및-바인딩)
  * [작동원리](#작동원리)
  * [JobParameters를 어떻게 사용가능한가.](#jobparameters를-어떻게-사용가능한가)
    * [contribution, chunkContext 차이](#contribution-chunkcontext-차이)
  * [jar로 만들어서 jobParameters 넘겨보기](#jar로-만들어서-jobparameters-넘겨보기)
  * [JobExecution](#jobexecution)
    * [JobInstance와 관계](#jobinstance와-관계)
    * [프로세스](#프로세스)
    * [TEST](#test-1)
* [STEP](#step)
  * [기본개념](#기본개념)
  * [기본구현체](#기본구현체)
    * [참고](#참고)
  * [Tasklet 구현](#tasklet-구현)
  * [Debug](#debug)
* [StepExecution](#stepexecution)
  * [기본 개념](#기본-개념)
  * [BATCH_STEP_EXECUTION 테이블과 매핑](#batchstepexecution-테이블과-매핑)
  * [성공, 실패 케이스별 DB테이블 데이터](#성공-실패-케이스별-db테이블-데이터)
  * [StepExecution 속성](#stepexecution-속성)
  * [TEST](#test-2)
* [StepContribution](#stepcontribution)
  * [기본개념](#기본개념-1)
  * [구조](#구조)
  * [흐름도](#흐름도)
* [ExecutionContext](#executioncontext)
  * [기본개념](#기본개념-2)
  * [테이블 맵핑](#테이블-맵핑)
  * [Job, Step별 공유관계 그림](#job-step별-공유관계-그림)
* [JobRepository](#jobrepository)
  * [기본 개념](#기본-개념-1)
  * [설정](#설정)
  * [BasicBatchConfigurer In Spring Batch 5.x](#basicbatchconfigurer-in-spring-batch-5x)
  * [개념 알아보기](#개념-알아보기)
* [JobLauncher](#joblauncher)
  * [기본 개념](#기본-개념-2)
  * [동기/비동기 실행](#동기비동기-실행)
    * [동기 실행](#동기-실행)
      * [동기 프로세스](#동기-프로세스)
    * [비동기 실행](#비동기-실행)
      * [바동기 프로세스](#바동기-프로세스)
<!-- TOC -->
# 출처
모든 내용과 사진자료는 inflearn 스프링배치(정수원) 참고하여 작성하였습니다.
https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B0%B0%EC%B9%98

# SpringBatch 환경준비
## Mysql Docker로 띄우기
```
docker run --name mysql -e MYSQL_ROOT_PASSWORD=password -d -p 3306:3306 mysql:latest
```

# @EnableBatchProcessing
SpringBatch 5.x 이후 @EnableBatchProcessing을 필수적으로 쓰지 않아도 된다.
> 참고
> > spring-batch Reference Doc: https://docs.spring.io/spring-batch/reference/index.html
> > spring-batch api Doc: https://docs.spring.io/spring-batch/docs/current/api/
> 
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

# JobParameter
1. 파라미터를 가진 도메인 객체
2. 하나의 Job에 존재할 수 있는 여러개의 JobInstance를 구분하기 위한 용도
3. JobInstance:JobParameter = 1:1
4. BATCH_JOB_EXECUTION_PARAMS 테이블에 저장됨

## 생성 및 바인딩
1. 어플리케이션 실행 시 주입
 - java -jar Batch.jar requestDate=20240420 -> JobParameter 객체화
2. 코드로 생성
 - JobParameterBuilder, DefaultJobParametersConverter
3. SpEL 이용
 - @Value("#{jobParameter[requestDate]}"), @JobScope,@StepScope 선언 필수

## 작동원리
![Spring JobParameter.png](doc%2Fpic%2FSpring%20JobParameter.png)
- 3.0 버전 그림이므로 추후 수정한다.

## JobParameters를 어떻게 사용가능한가.
Step에서 new Tasklet 부분을 살펴보면
```java
@Configuration
public class JobParameterConfiguration(){
    // ...
    public Step jobParameterStep1() {
        return new StepBuilder("jobParameterStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobParameterStep1 Start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
    // ...
}
```
tasklet의 파라미터인 StepContribution contribution, ChunkContext chunkContext 에서 사용가능하다.
> ex) StepContribution을 따라가보면 StepExecution 객체가 있고, 또 StepExecution을 따라가보면 
> JobExecution이 있음. 이안에 가보면 JobParameters 객체가 초기화되는것을 알 수 있다.   
> ChunkContext도 마찬가지이다.

### contribution, chunkContext 차이
```java
JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();
```
위에서 보면 서로 반환타입이 다른것을 알 수 있다.
DEBUG 모드로 자세히 확인해보면 된다.

> Type이 다르면 뭐가 다른걸까


## jar로 만들어서 jobParameters 넘겨보기
gradle > bootJar해서 jar파일을 생성한다.
build 디렉토리에 생성되어 있다.

터미널에서 jobParameter를 줘서 실행시켜본다.
이거 할때 JobParameterTest의 @Component풀어줘서 기존 파라미터 초기화가 안먹도록 해주자.
```shell
java -jar spring-batch.jar 'name=aa date(date)=2024-04-21 seq(long)=2L age(double)=10.0'
```
> jobParameters를 줄때 spring batch에서 type인식할 수 있도록 (괄호)안에 type 명시한다.   
> 쉘별로 파라미터를 주는 방식의 차이가 조금 있는것같음
> zsh라서 파라미터를 ''로 감싸주어야 에러가 나지 않는다.  
> bash는 감싸지 않아도 잘 되는듯

## JobExecution
1. JobInstance에 대한 한번의 시도를 의미하는 객체
2. Job 실행중 발생한 정보들을 저장하고 있는 객체

### JobInstance와 관계
> JobExecution 결과 COMPLETED면 JobInstance 실행완료로 동일 JobParameter로 재실행 불가  
> FAILED면 재실행 가능
> - JobParameter가 동일 값이더라도 JobInstance 실행 가능  
>
> COMPLETED가 될떄까지 하나의 JobInstance 내에서 여러번의 시도 가능

### 프로세스
![Spring JobExecution.png](doc%2Fpic%2FSpring%20JobExecution.png)

### TEST
Step에서 throw RuntimeException(); 이 후 여러번 실행시켜본다.
JOB_INSTANCE는 1개
JOB_EXECUTION* 테이블들의 ROW는 실행횟수만큼 되어있는것을 확인해볼 수 있다.
(성공건으로 하면 1Row만 있어서 강제로 에러로 테스트함)

# STEP
## 기본개념
1. Batch Job을 구성하는 독립적인 하나의 단계
2. 실제 배치처리 정의하고 컨트롤 하는데 필요한 모든정보를 담고있는 도메인 객체
3. 배치작업을 어떻게 구성하고 실행할 것인지 Job의 세부작업을 Task 기반으로 설정하고 명세해 놓은 객체
4. 모든 Job은 하나 이상의 Step으로 구성됨

## 기본구현체
1. TaskletStep
   - Tasklet 타입 구현체 제어
2. PartitionStep
   - 멀티쓰레드 방식으로 Step을 여러개로 분리해서 실행함
3. JobStep
   - Step 내에서 Job을 실행
4. FlowStep
   - Step 내에서 Flow를 실행

### 참고
![Step 구현체.png](doc%2Fpic%2FStep%20%EA%B5%AC%ED%98%84%EC%B2%B4.png)

## Tasklet 구현
new Tasklet해서 익명클래스로 구현해도 되고, Custom Tasklet class를 만들어서 실행해도 된다.

## Debug

# StepExecution
## 기본 개념
1. JobExecution 과 마찬가지로 Step에 대한 한번의 시도를 의미하는 객체
2. Step 실행 중에 발생한 정보들을 저장하는 객체
    - 시작, 종료시간, 상태, commit count, rollback count 등
3. Step 실행마다 매번 생성됨
4. Job 재시작시 성공한 Step은 실행안되고, 실패한 Step은 실행된다.
    - 별도의 옵션을 통해서 성공 Step도 재시작시 실행되도록 할 수 있다.
5. Step이 실제로 시작했을때만 생성된다
    - 이전단계 Step이 실패해서 현재 Step이 실행안됐으면 StepExecution은 생성되지 않는다.
6. JobExecution과의 관계
    - StepExecution이 모두 정상이어야 JobExecution 정상 완료
    - StepExecution 하나라도 실패시 JobExecution 실패

## BATCH_STEP_EXECUTION 테이블과 매핑
1. JobExecution:StepExecution = 1:M
2. 하나의 Job에 다수 Step이면 각 StepExecution은 하나의 JobExecution을 부모로 가진다.

## 성공, 실패 케이스별 DB테이블 데이터
![케이스별 테이블 데이터.png](doc%2Fpic%2F%EC%BC%80%EC%9D%B4%EC%8A%A4%EB%B3%84%20%ED%85%8C%EC%9D%B4%EB%B8%94%20%EB%8D%B0%EC%9D%B4%ED%84%B0.png)
## StepExecution 속성
![StepExecution 속성.png](doc%2Fpic%2FStepExecution%20%EC%86%8D%EC%84%B1.png)
## TEST
- 시나리오
  - JobInstance A, JobInstance B(동일 Job, 다른 파라미터)
  - JobInstance B의 Job Execution 2번(첫번째 시도 FAILED, 두번째 COMPLETED)
  - StepExecution이 기대한 값으로 업데이트 되었는지 확인해본다.
  
- 프로세스(그림)
![StepExecution Test.png](doc%2Fpic%2FStepExecution%20Test.png)

# StepContribution
## 기본개념
1. Chunk Process의 변경사항을 버퍼링 한 후 StepExecution 상태를 업데이트하는 도메인 객체
2. Chunk Commit 직전에 StepExecution의 apply 메소드를 호출하여 상태 업데이트함
3. ExitStatus의 기본 종료코드 외 사용자정의 종료코드 생성해서 적용 가능함

## 구조
```java
public class StepContribution implements Serializable {
    // 성공적으로 read한 item 수
    private volatile long readCount = 0;
    // 성공적으로 write한 item 수
    private volatile long writeCount = 0;   
    // ItemProcessor에 의해 filtering 된 item 수
    private volatile long filterCount = 0;
    // 부모클래스인 StepExecution의 총 skip 횟수
    private final long parentSkipCount;

    private volatile long readSkipCount;

    private volatile long writeSkipCount;

    private volatile long processSkipCount;

    private ExitStatus exitStatus = ExitStatus.EXECUTING;
    // StepExecution 객체도 저장된다.
    private final StepExecution stepExecution;

    /**
     * @param execution {@link StepExecution} the stepExecution used to initialize
     * {@code skipCount}.
     */
    public StepContribution(StepExecution execution) {
        this.stepExecution = execution;
        this.parentSkipCount = execution.getSkipCount();
    }
    // ......
}
```
## 흐름도
![StepContribution 흐름도.png](doc%2Fpic%2FStepContribution%20%ED%9D%90%EB%A6%84%EB%8F%84.png)

# ExecutionContext
## 기본개념
1. 스프링배치 프레임워크에서 관리하는 key/value 컬렉션으로 Step,Job Execution 객체의 상태를 저장하는 공유객체
2. 공유범위
    - Job: 각 Job의 JobExecution에 저장되며 Job간 서로 공유 X, Job의 Step 간 공유됨
    - Step: 각 Step의 StepExecution에 저장되며 Step간 서로 공유 안됨 

## 테이블 맵핑
연관테이블
- BATCH_JOB_EXECUTION_CONTEXT
- BATCH_STEP_EXECUTION_CONTEXT
![ExecutionContext Table.png](doc%2Fpic%2FExecutionContext%20Table.png)

## Job, Step별 공유관계 그림
![Job Step별 ExecutionContext 공유관계.png](doc%2Fpic%2FJob%20Step%EB%B3%84%20ExecutionContext%20%EA%B3%B5%EC%9C%A0%EA%B4%80%EA%B3%84.png)
**sorc보고 debug해서 각각 공유관계를 봐보면 이해하기 쉬움**

# JobRepository
## 기본 개념
1. 배치작업 정보를 저장하는 저장소 역할
2. 모든 meta data를 저장함

## 설정
1. 스프링 배치에서 자동으로 JobRepository가 빈으로 생성됨

## BasicBatchConfigurer In Spring Batch 5.x
BasicBatchConfigurer 삭제되어 DefaultBatchConfiguration 클래스를 확장하여 구현가능하다.

> jobRepositoryFactoryBean.afterPropertiesSet();  
> set한 속성값 외 필요속성값들을 자동으로 초기화해준다.

## 개념 알아보기
BatchAutoConfiguration 클래스는  
> @ConditionalOnMissingBean(value = DefaultBatchConfiguration.class, annotation = EnableBatchProcessing.class)   

해당 어노테이션에 의해서 DefaultBatchConfiguration 상속하여 빈등록이나, @EnableBatchProcessing 이 들어가면 실행이 안된다.  
따라서 Job을 실행해주는 JobLauncher도 실행이 안됨. 따로 구현해줘야함

# JobLauncher
## 기본 개념
1. 배치 Job을 실행시키는 역할
2. (Job, JobParameters)
3. 배치작업을 수행한 후 최종 client에게 JobExecution을 반환한다.
4. 스프링 배치 구동시 JobLauncher 빈이 자동으로 생성된다.
5. Job 실행
    - JobLauncher.run(Job, JobParameters)
    - JobLauncherApplicationRunner가 자동으로 JobLauncher을 실행시킨다.

## 동기/비동기 실행
### 동기 실행
    - taskExecutor를 SyncTaskExecutor로 설정할 경우(기본 - SyncTaskExecutor)
    - JobExecution을 획득하고 배치처리를 최종 완료한 이후 Client에거 JobExecution 반환
    - 스케줄러에 의한 배치처리에 적합
#### 동기 프로세스
![동기Process.png](doc%2Fpic%2F%EB%8F%99%EA%B8%B0Process.png)

### 비동기 실행
    - taskExecutor가 SimpleAsyncTaskExecutor로 설정할 경우
    - JobExecution을 획득한 후 Client에게 바로 JobExecution을 반환하고 배치처리를 완료함
    - HTTP 요청에 의한 배치처리에 적합함
#### 바동기 프로세스
![비동기Process.png](doc%2Fpic%2F%EB%B9%84%EB%8F%99%EA%B8%B0Process.png)

## TEST
### Controller
Client 입장에서 동기, 비동기 수행을 보기 위해 Controller 구현한다.
```java
@RestController
public class JobLauncherController {

    private final Map<String, Job> jobMap;
    private final JobLauncher jobLauncher;
    // 비동기 TaskExecutor 설정을 위한 변수
    private final TaskExecutorJobLauncher taskExecutorJobLauncher;

    // 비동기 TaskExecutor 설정을 위한 생성자
    public JobLauncherController(Map<String, Job> jobMap, JobLauncher jobLauncher, DefaultBatchConfiguration defaultBatchConfiguration) {
        this.jobMap = jobMap;
        this.jobLauncher = jobLauncher;
        taskExecutorJobLauncher = (TaskExecutorJobLauncher) defaultBatchConfiguration.jobLauncher();
    }

    // 동기 실행을 위한 생성자
//    public JobLauncherController(Map<String, Job> jobMap, JobLauncher jobLauncher) {
//        this.jobMap = jobMap;
//        this.jobLauncher = jobLauncher;
//    }

    @PostMapping("/batch/sync")
    public String launchSync(@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", member.getId())
                .addDate("date", new Date())
                .toJobParameters();

        Job job = jobMap.get("jobLauncherJob");
        jobLauncher.run(job, jobParameters);
        return "batch completed";
    }

    @PostMapping("/batch/async")
    public String launchAsync(@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", member.getId())
                .addDate("date", new Date())
                .toJobParameters();

        Job job = jobMap.get("jobLauncherJob");
        taskExecutorJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        taskExecutorJobLauncher.run(job, jobParameters);

        return "batch completed";
    }
}

```

### TEST & DEBUG
동기방식과 비동기 방식은 TaskExcutor가 다른다.
별도 설정이 없다면, TaskExecutor가 SyncTaskExecutor로 설정됨.
비동기 설정을 하고싶다면 AsyncTaskExecutor로 설정해주고 실행한다.

Client 입장에서 동기/비동기 테스트를 위해서 Step1에 Thread.sleep을 걸어준다.
두 방식의 응답속도 차이를 비교한다.
예상값: 싱크는 5초 슬립이 있으므로 슬립시간까지 더해진다.   

![sync async 응답속도 차이.png](doc%2Fpic%2Fsync%20async%20%EC%9D%91%EB%8B%B5%EC%86%8D%EB%8F%84%20%EC%B0%A8%EC%9D%B4.png)


# 의문점
## application.yml의 spring.job.name 은 어떤 기능이 있을까?
실제 JobLauncher를 구현해서 Job을 실행할때, 'job'이라는 BeanId로 등록된 Job이 실행됨.   
수동으로 BatchAutoConfiguration을 통한 job실행때만 작동하는 것인가?

# 배치 초기화
## JobLauncherApplicationRunner
1. BatchAutoConfiguration에서 생성됨
2. ApplicationRunner의 구현체
3. 어플리케이션 정상구동시 실행됨
4. 기본적으로 Bean 등록된 모든 Job 실행시킴
-> 아닌거같은데, ,,, 테스트 해볼것

## BatchProperties
1. Job이름, 스키마 초기화, table prefix 설정 가능

# JobBuilder
## 기본 개념
1. Job을 구성하는 설정 조건에 따라 두개의 하위 빌더 클래스를 생성하고 Job생성을 위임한다.
2. 실제 Job을 생성하는 것은 JobBuilder가 아니고 하단의 Builder이다.
- SimpleJobBUilder
  - SimpleJob 생성
  - Job 실행 관련 여러 실행 API 제공
- FlowJobBuilder
  - FlowJob 생성
  - Flow 실행과 관련된 여러 API 제공

## 프로세스
3.0 기준이므로 새로 만들어본다.

## TEST
SimpleJobBuilder.build() 되는 과정 debug
FlowjobBuilder.build() 되는 과정 debug

# SimpleJob
## 기본개념
1. Step을 실행시키는 Job구현체로 SimpleJobBuilder에 의해 생성된다.
2. 여러단계의 Step으로 구성되며 Step을 순차적으로 실행시킨다.
3. 모든 Step이 정상적으로 완료되어야 Job이 완료된다.
4. 맨마지막 Step의 BatchStatusrk Job의 최종 BatchStatus가 된다.

## 흐름

## API4
```java
@Configuration
public class batchJobConfiguration() {
    public Job batchJob() {
        return new JobBuilder("batchJob", jobRepository)
                .start(Step) // 처음 실행할 Step 설정, SimpleJobBuilder 반환
                .next(Step) // 다음 실행할 Step 설정, 횟수제한 없으며 모든 next의 step이 종료되면 Job 종료
                .incrementer(JobParametersIncrementer) // JobParameter값을 자동증가해주는 Incrementer 설정
                .preventRestart(true) // Job 재시작 가능여부, 디폴트는 True
                .validator(JobParameterValidator) // 검증용 Validator
                .listener(JobExecutionListener) // Job 라이프사이클 특정시점 콜백받는 JobExecutionListner 설정
                .build(); // SimpleJob 생성
        }    
}
```

## start(), next()
### start()
1. 처음 실행 할 Step 설정으로 최초 한번 설정함
2. 매개변수에 따라 종류별 JobBuilder가(SimpleJob, FlowJob, ...) 생성되고 반환된다.

### next()
1. 여러번 사용 가능

## validator()
1. Job실행에 필요한 파라미터(JobParameters)를 검증
2. DefaultJobParametersValidator 구현체를 지원함
3. 좀 더 복잡한 제약조건이 있다면 인터페이스 직접 구현 가능함

### 구조
JobParameters 값을 매개변수로 받아 검증함
```java
void validate(@Nullable JobParameters jobParameters);
```

### 흐름도

## preventRestart()
1. Job의 재시작 여부를 설정
2. 일반적으로 실패한 Job은 완료시까지 재실행 할 수 있다.
3. false 처리하면 어떤 경우든 재시작할 수 없다.
4. 재시작과 관련된 기능으로 처음시작과는 상관없음.

## incrementer()
1. JobParameters에서 필요한 값을 증가시켜 다음에 사용될 JobParameters 오브젝트 리턴
2. 기존의 JobParameter 변경없이 Job을 여러번 시작하고자 할때
3. RunIdIncrementer 구현체를 지원하면 인터페이스를 직접 구현할 수 있다.

### 구조
JobParametersIncrementer class
JobParameters getNext(@Nullalble JobParameters parameters);

### 실행
.incrementer()로 설정해준다.
매번 JobParameters 가 바뀐다.

## SimpleJob 아키텍처
### SimpleJob 흐름도

# StepBuilder

