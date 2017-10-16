package org.bttf.botlogger;

import org.bttf.botlogger.model.OrderLogEntry;
import org.bttf.botlogger.model.OrderState;
import org.eclipse.collections.impl.factory.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Service
public class BotloggerController {

    private OrderLogEntry lastOrderEntry = null;

    private List<OrderState> completedOrders = Lists.mutable.of();

    @PutMapping("/order/log")
    @ResponseBody
    public void logConversationId(@RequestBody OrderLogEntry orderLogEntry){
        this.lastOrderEntry = orderLogEntry;
        if (isOrderCompleted(orderLogEntry)){
            handleCompletedOrder(orderLogEntry);
        }
    }

    private void handleCompletedOrder(OrderLogEntry orderLogEntry) {
        /*
            TODO:: Accommodate order price
         */
        completedOrders.add(orderLogEntry.lastOrderState);
    }

    @GetMapping("/order/last")
    @ResponseBody
    public OrderLogEntry getLastOrderLogEntry(){
        return lastOrderEntry;
    }

    @GetMapping("/reset")
    public void reset(){
        lastOrderEntry = null;
        completedOrders.clear();
    }

    @GetMapping("/orders")
    @ResponseBody
    public List<OrderState> getOrders(){
        return completedOrders;
    }

    private static boolean isOrderCompleted(OrderLogEntry orderLogEntry) {
        return orderLogEntry!=null
                && orderLogEntry.lastOrderState!=null
                && orderLogEntry.lastOrderState.completed!=null
                && orderLogEntry.lastOrderState.completed;
    }
}
