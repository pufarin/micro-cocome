package net.gabrielkovacs.showDeliveryReports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"net.gabrielkovacs.common", "net.gabrielkovacs.showDeliveryReports" })
@EnableJpaRepositories(basePackages={"net.gabrielkovacs.common.repository"})
@EntityScan(basePackages={"net.gabrielkovacs.common.entities"})
public class ShowDeliveryReportsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowDeliveryReportsApplication.class, args);
	}

}
