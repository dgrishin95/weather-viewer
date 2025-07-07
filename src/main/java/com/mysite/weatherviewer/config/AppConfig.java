package com.mysite.weatherviewer.config;

import java.util.Properties;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class AppConfig {

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

    @Value("${hibernate.format-sql}")
    private String hibernateFormatSql;

    @Value("${hibernate.hbm2ddl-auto}")
    private String hibernateHbm2ddlAuto;

    @Value("${hibernate.jdbc.lob.non-contextual-creation}")
    private String hibernateLobNonContextualCreation;

    @Value("${hibernate.temp.use-jdbc-metadata-defaults}")
    private String hibernateTempUseJdbcMetadataDefaults;

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
    @DependsOn("liquibase")  // Указываем зависимость
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.mysite.weatherviewer.entity");
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
        properties.put("hibernate.format_sql", hibernateFormatSql);
        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        properties.put("hibernate.jdbc.lob.non_contextual_creation", hibernateLobNonContextualCreation);
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", hibernateTempUseJdbcMetadataDefaults);

        return properties;
    }
}