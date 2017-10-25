package org.bttf.botlogger;

import org.bttf.botlogger.model.OrderLogEntry;
import org.bttf.botlogger.model.OrderState;
import org.bttf.botlogger.util.OrderUtil;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.utility.Iterate;
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
    public Map<String, String> getHoldingForStock(@RequestParam String stock) {

        MutableList<OrderState> filteredOrders = Iterate.select(completedOrders,
                o -> o.getStock().equalsIgnoreCase(stock), Lists.mutable.of());

        if (filteredOrders.isEmpty()){
            return Maps.mutable.of();
        }
        long totalQty = filteredOrders.collectLong(OrderUtil::getQtyWithDirection).sum();
        long totalVolume = filteredOrders.collectLong(OrderState::getQty).sum();
        double totalCost = filteredOrders.collectDouble((o)->o.getQty()*o.getPrice()).sum();
        double avgPrice = totalVolume==0?0:totalCost/totalVolume;
        return Maps.mutable.of(
                "qty", ""+totalQty,
                "avgPrice", ""+avgPrice);
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
    @ResponseBody
    public boolean reset(){
        lastOrderEntry = null;
        completedOrders.clear();
        initTestOrders();
        return true;
    }

    public void initTestOrders(){
        submitTestOrder("Apple", 5000L,170.43,"Buy", "2017-10-24T13:21:22.173Z");
        submitTestOrder("ibm", 2000L, 193.02, "Buy", "2017-10-24T12:43:00.123Z");
        submitTestOrder("Apple", 1000L, 175.89,"Sell", "2017-10-24T13:21:22.173Z");
    }

    private void submitTestOrder(String stock, Long qty, double price, String direction, String timestamp) {
        OrderState order = new OrderState();
        order.setCompleted(true);
        order.setDirection(direction);
        order.setQty(qty);
        order.setPrice(price);
        order.setStock(stock);
        order.setTimestamp(timestamp);
        OrderLogEntry logEntry = new OrderLogEntry();
        logEntry.setLastUserMessage("I want to do something");
        logEntry.setLastSystemMessage("What stock do you want to sell?");
        logEntry.setChannel("AlexChannel");
        logEntry.setLastOrderState(order);
        logOrderEntry(logEntry);
    }

    private static boolean isOrderCompleted(OrderLogEntry orderLogEntry) {
        return orderLogEntry!=null
                && orderLogEntry.getLastOrderState()!=null
                && orderLogEntry.getLastOrderState().getCompleted()!=null
                && orderLogEntry.getLastOrderState().getCompleted();
    }

    private void handleCompletedOrder(OrderLogEntry orderLogEntry) {
        completedOrders.add(orderLogEntry.getLastOrderState());
    }
}
