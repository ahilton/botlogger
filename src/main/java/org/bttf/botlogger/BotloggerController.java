package org.bttf.botlogger;

import org.bttf.botlogger.model.OrderLogEntry;
import org.bttf.botlogger.model.OrderState;
import org.bttf.botlogger.util.OrderUtil;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Service
public class BotloggerController {

    private OrderLogEntry lastOrderEntry = null;

    private List<OrderState> completedOrders = Lists.mutable.of();

    /*
     *  LOG ORDERS FROM BOT
     *
     */
    @PutMapping("/order/log")
    @ResponseBody
    @CrossOrigin
    public void logOrderEntry(@RequestBody OrderLogEntry orderLogEntry){
        this.lastOrderEntry = orderLogEntry;
        if (isOrderCompleted(orderLogEntry)){
            handleCompletedOrder(orderLogEntry);
        }
    }

    /*
     *  ORDER QUERY END POINTS
     *
     */
    @GetMapping("/order/last")
    @ResponseBody
    @CrossOrigin
    public OrderLogEntry getLastOrderLogEntry(){
        return lastOrderEntry;
    }

    @GetMapping("/orders")
    @ResponseBody
    @CrossOrigin
    public List<OrderState> getOrders(){
        return completedOrders;
    }

    @GetMapping("/stock/holding")
    @ResponseBody
    @CrossOrigin
    public Long getHoldingForStock(@RequestBody String stock) {
        MutableList<OrderState> filledOrders = Lists.mutable.ofAll(completedOrders);
        return filledOrders
                .asLazy()
                .select(o->o.getStock().toLowerCase().equals(stock.toLowerCase()))
                .collectLong(OrderUtil::getQtyWithDirection)
                .sum();
    }

    @GetMapping("/holdings")
    @ResponseBody
    @CrossOrigin
    public Map<String, Long> getStockHoldings() {
        MutableList<OrderState> filledOrders = Lists.mutable.ofAll(completedOrders);
        return filledOrders
                .aggregateBy(
                        OrderState::getStock, // aggregation key
                        () -> 0L, // initial value
                        (l, o) -> l+OrderUtil.getQtyWithDirection(o) // summing function
                );
    }

    /*
     *  UTILITY
     *
     */
    @GetMapping("/reset")
    @CrossOrigin
    public void reset(){
        lastOrderEntry = null;
        completedOrders.clear();
    }

    private static boolean isOrderCompleted(OrderLogEntry orderLogEntry) {
        return orderLogEntry!=null
                && orderLogEntry.getLastOrderState()!=null
                && orderLogEntry.getLastOrderState().getCompleted()!=null
                && orderLogEntry.getLastOrderState().getCompleted();
    }

    private void handleCompletedOrder(OrderLogEntry orderLogEntry) {
//        Double price = (new Random().nextDouble() + 0.1) * 100d;
//        OrderState completedOrder = orderLogEntry.getLastOrderState();
//        completedOrder.setPrice(price);
        completedOrders.add(orderLogEntry.getLastOrderState());
    }
}
