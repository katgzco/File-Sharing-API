package com.filesharing.filesharingapi;

import com.filesharing.filesharingapi.exception.storage.StorageInitializationException;
import com.filesharing.filesharingapi.storage.IStorageService;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "File Sharing API",
				version = "v1",
				description = "MVP file sharing API",

				license=@License(
				name="Apache 2.0"
				)
			),
			externalDocs = @ExternalDocumentation(
					description = "Strike Team :)",
					url = "https://strike.sh/"

			)
)
public class FileSharingApiApplication {

	public static void main(String[] args) {

//		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//		IStorageService storageService = context.getBean(IStorageService.class);
//		try {
//			storageService.initialize();
//		} catch (StorageInitializationException e) {
//			// Log the exception
//			//log.error("Failed to initialize storage service", e);
//			System.exit(1); // Halt the application if critical service can't start
//		}
		SpringApplication.run(FileSharingApiApplication.class, args);
	}

}
