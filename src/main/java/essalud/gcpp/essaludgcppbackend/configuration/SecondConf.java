package essalud.gcpp.essaludgcppbackend.configuration;

import java.util.Properties;

import javax.sql.DataSource;

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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		//basePackages = "essalud.gcpp.essaludgcppbackend.repositoriesquery",
		entityManagerFactoryRef = "secondEntityManagerFactory",
		transactionManagerRef = "secondTransactionManager")
public class SecondConf {

	@Bean(name = "secondDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.second")
	public DataSource secondDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "secondEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
			@Qualifier("secondDataSource") DataSource secondDataSource) {

		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(secondDataSource);
		emf.setPackagesToScan("essalud.gcpp.essaludgcppbackend.model");
		emf.setPersistenceUnitName("second");
		emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "none");
		jpaProperties.setProperty("hibernate.show_sql", "false");
		emf.setJpaProperties(jpaProperties);

		return emf;
	}

	@Bean(name = "secondTransactionManager")
	public PlatformTransactionManager secondTransactionManager(
			@Qualifier("secondEntityManagerFactory") LocalContainerEntityManagerFactoryBean secondEntityManagerFactory) {
        assert secondEntityManagerFactory.getObject() != null;
        return new JpaTransactionManager(secondEntityManagerFactory.getObject());
	}

	@Bean(name = "secondJdbcTemplate")
	public JdbcTemplate secondJdbcTemplate(@Qualifier("secondDataSource") DataSource secondDataSource) {
		return new JdbcTemplate(secondDataSource);
	}

	@Bean(name = "secondNamedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate secondNamedParameterJdbcTemplate(
			@Qualifier("secondDataSource") DataSource secondDataSource) {
		return new NamedParameterJdbcTemplate(secondDataSource);
	}
}
