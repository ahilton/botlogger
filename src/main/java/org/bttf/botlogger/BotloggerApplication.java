package org.bttf.botlogger;

import org.bttf.botlogger.model.OrderLogEntry;
import org.bttf.botlogger.model.OrderState;
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
		submitTestOrder(controller, "Apple", 5000L,170.43,"Buy");
		submitTestOrder(controller, "ibm", 100L, 193.02, "Buy");
		submitTestOrder(controller, "Apple", 1000L, 175.89,"Sell");
	}

	private void submitTestOrder(BotloggerController controller, String stock, Long qty, double price, String direction) {
		OrderState order = new OrderState();
		order.setCompleted(true);
		order.setDirection(direction);
		order.setQty(qty);
		order.setPrice(price);
		order.setStock(stock);
		OrderLogEntry logEntry = new OrderLogEntry();
		logEntry.setLastUserMessage("I want to do something");
		logEntry.setLastSystemMessage("What stock do you want to sell?");
		logEntry.setChannel("AlexChannel");
		logEntry.setLastOrderState(order);
		controller.logOrderEntry(logEntry);
	}
}
