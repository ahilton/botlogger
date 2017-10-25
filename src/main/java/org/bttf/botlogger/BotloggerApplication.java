package org.bttf.botlogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class BotloggerApplication {

	@Autowired
	BotloggerController controller;

	public static void main(String[] args) {
		SpringApplication.run(BotloggerApplication.class, args);
	}

	@PostConstruct
	public void initTestOrders(){
		controller.initTestOrders();
	}
}
