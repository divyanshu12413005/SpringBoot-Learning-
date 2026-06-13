package com.divyanshu.youtube.InternalWorkingSpringBoot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternalWorkingSpringBootApplication  implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(InternalWorkingSpringBootApplication.class, args);
	}

//	@Autowired   //Field Injection use this in place of construction dependencies
//private final RazorpayPaymentService paymentserivce;
private final PaymentService paymentserivce;


	//this is a construction dependencies injection
	public InternalWorkingSpringBootApplication(PaymentService paymentserivce) {
		this.paymentserivce = paymentserivce;
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Payment Done!" + paymentserivce.pay());

	}
}
