package com.mysite.weatherviewer.config;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
        "com.mysite.weatherviewer.mapper",
        "com.mysite.weatherviewer.service",
        "com.mysite.weatherviewer.repository",
        "com.mysite.weatherviewer.client",
        "com.mysite.weatherviewer.interceptor"
})
@PropertySource("classpath:application-test.properties")
public class TestConfig {

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String databaseUsername;

    @Value("${database.password}")
    private String databasePassword;

    @Value("${database.driver-class-name}")
    private String databaseDriverClassName;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show-sql}")
    private String hibernateShowSql;

    @Value("${hibernate.hbm2ddl-auto}")
    private String hibernateHbm2ddlAuto;

    @Value("${hibernate.jdbc.lob.non-contextual-creation}")
    private String hibernateLobNonContextualCreation;

    @Value("${hibernate.temp.use-jdbc-metadata-defaults}")
    private String hibernateTempUseJdbcMetadataDefaults;

    @Value("${hibernate.jdbc.batch-size}")
    private String hibernateJdbcBatchSize;

    @Value("${hibernate.order-updates}")
    private String hibernateOrderUpdates;

    @Value("${liquibase.change-log}")
    private String liquibaseChangeLog;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        dataSource.setDriverClassName(databaseDriverClassName);

        return dataSource;
    }

    @Bean("liquibase")
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog(liquibaseChangeLog);

        return liquibase;
    }

    @Bean
    @DependsOn("liquibase")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.mysite.weatherviewer.model");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);

        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        properties.put("hibernate.jdbc.lob.non_contextual_creation", hibernateLobNonContextualCreation);
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", hibernateTempUseJdbcMetadataDefaults);
        properties.put("hibernate.jdbc.batch_size", hibernateJdbcBatchSize);
        properties.put("hibernate.order_updates", hibernateOrderUpdates);

        return properties;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        });

        return restTemplate;
    }
}
