package com.yzl.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.hasor.spring.boot.EnableHasor;
import net.hasor.spring.boot.EnableHasorWeb;

@EnableHasor()
@EnableHasorWeb()
@SpringBootApplication()//scanBasePackages = { "net.example.hasor" }
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}


