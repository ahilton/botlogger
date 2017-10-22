package org.bttf.botlogger.util;

import org.bttf.botlogger.model.OrderState;

public class OrderUtil {

    public static Long getQtyWithDirection(OrderState order){
        return order.getQty() * getDirectionSign(order);
    }

    public static Long getDirectionSign(OrderState order){
        return order.getDirection().toLowerCase().equals("sell")?-1L:1L;
    }

    public static Double getPrice(OrderState order){
        return order.getPrice();
    }
}
