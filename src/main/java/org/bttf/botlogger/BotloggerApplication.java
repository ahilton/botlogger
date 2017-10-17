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
		submitTestOrder(controller, "Apple", 5000L, "Buy");
		submitTestOrder(controller, "ibm", 100L, "Buy");
		submitTestOrder(controller, "Apple", 1000L, "Sell");
	}

	private void submitTestOrder(BotloggerController controller, String stock, Long qty, String direction) {
		OrderState order = new OrderState();
		order.setCompleted(true);
		order.setDirection(direction);
		order.setQty(qty);
		order.setStock(stock);
		OrderLogEntry logEntry = new OrderLogEntry();
		logEntry.setLastOrderState(order);
		controller.logOrderEntry(logEntry);
	}
}
