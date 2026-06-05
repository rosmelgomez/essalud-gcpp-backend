package essalud.gcpp.essaludgcppbackend.serviceimplements;

import javax.sql.DataSource;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;


@Component
public class ExecuteQuery implements ApplicationRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteQuery.class);

	private final DataSource firstDataSource;

	public ExecuteQuery(@Qualifier("firstDataSource") DataSource firstDataSource) {
		this.firstDataSource = firstDataSource;
	}

	@Override
	public void run(@NonNull ApplicationArguments args) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("core/insert_users.sql"));
		populator.addScript(new ClassPathResource("core/insert_rol.sql"));
		populator.addScript(new ClassPathResource("core/insert_user_rol.sql"));
		populator.addScript(new ClassPathResource("encuesta/insert_modulo.sql"));
		populator.addScript(new ClassPathResource("encuesta/insert_categoria.sql"));
		populator.addScript(new ClassPathResource("encuesta/insert_subcategoria.sql"));
		populator.addScript(new ClassPathResource("encuesta/insert_variable.sql"));
		populator.addScript(new ClassPathResource("documento/insert_catalogo_extencion.sql"));

		try {
			populator.execute(firstDataSource);
			LOGGER.info("Archivos SQL ejecutado correctamente.");
		} catch (Exception exception) {
			LOGGER.error("Error al ejecutar archivos SQL", exception);
		}


	}
}
