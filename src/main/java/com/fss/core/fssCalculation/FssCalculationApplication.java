package com.fss.core.fssCalculation;

import com.fss.core.fssCalculation.persistance.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FssCalculationApplication {


    private  UserRepository userRepository;

	public static void main(String[] args) {



		SpringApplication.run(FssCalculationApplication.class, args);
		//System.out.println("Your Application started successfully");






		System.out.println("Your Application started successfully.");


	}

}
