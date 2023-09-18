package spa.spaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaserverApplication.class, args);
	}

}
