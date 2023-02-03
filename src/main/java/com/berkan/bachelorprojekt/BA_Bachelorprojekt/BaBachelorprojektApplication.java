package com.berkan.bachelorprojekt.BA_Bachelorprojekt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
/*@ComponentScan("com.berkan.bachelorprojekt.BA_Bachelorprojekt.Creation")
@EnableJpaRepositories (basePackages = {"com.berkan.bachelorprojekt.BA_Bachelorprojekt"})*/
public class BaBachelorprojektApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaBachelorprojektApplication.class, args);
	}

}
