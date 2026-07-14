package com.divyanshu.learnspringsecurityjwt;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class LearnSpringSecurityJwtApplication {



	public static void main(String[] args) {
		SpringApplication.run(LearnSpringSecurityJwtApplication.class, args);
	}




}