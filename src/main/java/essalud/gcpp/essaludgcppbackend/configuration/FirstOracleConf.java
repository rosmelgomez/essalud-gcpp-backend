package essalud.gcpp.essaludgcppbackend.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        //basePackages = "essalud.gcpp.essaludgcppbackend.repositories",
        entityManagerFactoryRef = "firstOracleEntityManagerFactory",
        transactionManagerRef = "firstOracleTransactionManager")
public class FirstOracleConf {

    @Bean("firstOracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.firstoracle")
    public DataSource firstOracleDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "firstOracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean firstOracleEntityManagerFactory(
            @Qualifier("firstOracleDataSource") DataSource firstOracleDataSource) {

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(firstOracleDataSource);
        emf.setPackagesToScan("essalud.gcpp.essaludgcppbackend.model");
        emf.setPersistenceUnitName("firstoracle");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        jpaProperties.setProperty("hibernate.show_sql", "false");
        emf.setJpaProperties(jpaProperties);

        return emf;
    }

    @Bean(name = "firstOracleTransactionManager")
    public PlatformTransactionManager firstOracleTransactionManager(
            @Qualifier("firstOracleEntityManagerFactory") LocalContainerEntityManagerFactoryBean firstOracleEntityManagerFactory){
        assert firstOracleEntityManagerFactory.getObject() != null;
        return new JpaTransactionManager(firstOracleEntityManagerFactory.getObject());
    }

    @Bean(name = "firstOracleJdbcTemplate")
    public JdbcTemplate firstOracleJdbcTemplate(@Qualifier("firstOracleDataSource") DataSource firstOracleDataSource) {
        return new JdbcTemplate(firstOracleDataSource);
    }

    @Bean(name = "firstOracleNamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate firstOracleNamedParameterJdbcTemplate(
            @Qualifier("firstOracleDataSource") DataSource firstOracleDataSource) {
        return new NamedParameterJdbcTemplate(firstOracleDataSource);
    }


}
