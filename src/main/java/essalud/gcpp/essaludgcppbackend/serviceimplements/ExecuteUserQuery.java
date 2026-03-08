package essalud.gcpp.essaludgcppbackend.serviceimplements;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;


@Component
public class ExecuteUserQuery implements ApplicationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteUserQuery.class);

	private final DataSource firstDataSource;

	public ExecuteUserQuery(@Qualifier("firstDataSource") DataSource firstDataSource) {
		this.firstDataSource = firstDataSource;
	}

	@Override
	public void run(ApplicationArguments args) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("insert_users.sql"));

		try {
			populator.execute(firstDataSource);
			LOGGER.info("Archivo SQL insert_users.sql ejecutado correctamente.");
		} catch (Exception exception) {
			LOGGER.error("Error al ejecutar insert_users.sql", exception);
		}


	}
}
