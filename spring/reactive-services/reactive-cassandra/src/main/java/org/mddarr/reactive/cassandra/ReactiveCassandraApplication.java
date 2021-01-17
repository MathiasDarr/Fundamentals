package org.mddarr.reactive.cassandra;

import org.mddarr.reactive.cassandra.webclients.EmployeeWebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveCassandraApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveCassandraApplication.class, args);
		EmployeeWebClient employeeWebClient = new EmployeeWebClient();
		employeeWebClient.consume();
	}

}
