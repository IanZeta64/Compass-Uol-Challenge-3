package br.com.compasso.posthistoryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaRepositories
@EnableAsync
public class PostHistoryApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostHistoryApiApplication.class, args);
	}

}
