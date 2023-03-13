package com.wak.chimplanet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

// dataSource 제외 임시옵션 추가
@SpringBootApplication
public class ChimPlanetBackApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChimPlanetBackApplication.class, args);
	}
}
