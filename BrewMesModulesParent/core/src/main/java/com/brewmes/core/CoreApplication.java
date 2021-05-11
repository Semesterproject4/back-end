package com.brewmes.core;

import com.brewmes.common_repository.BatchRepository;
import com.brewmes.common_repository.ConnectionRepository;
import com.brewmes.common_repository.ScheduleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.brewmes.*")
@EnableMongoRepositories(basePackageClasses = {BatchRepository.class, ConnectionRepository.class, ScheduleRepository.class})
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
