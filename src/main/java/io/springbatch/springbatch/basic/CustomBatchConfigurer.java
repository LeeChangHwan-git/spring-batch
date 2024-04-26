//package io.springbatch.springbatch.basic;
//
//import org.springframework.batch.core.configuration.BatchConfigurationException;
//import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class CustomBatchConfigurer extends DefaultBatchConfiguration {
//
//    private final DataSource dataSource;
//
//    public CustomBatchConfigurer(DataSource dataSource, PlatformTransactionManager transactionManager) {
//        this.dataSource = dataSource;
//    }
//
//    @Override
//    public JobRepository jobRepository() throws RuntimeException {
//        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
//        try {
//            jobRepositoryFactoryBean.setDataSource(dataSource);
//            jobRepositoryFactoryBean.setTransactionManager(getTransactionManager());
//            jobRepositoryFactoryBean.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
//            jobRepositoryFactoryBean.afterPropertiesSet();
////            jobRepositoryFactoryBean.setTablePrefix();
//            return jobRepositoryFactoryBean.getObject();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
