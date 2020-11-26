package br.south.test;
import java.io.FileNotFoundException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;




@SpringBootApplication
@EnableScheduling
public class TestApplication {

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(TestApplication.class, args);
	
	
	}

	
	
	
	

}
