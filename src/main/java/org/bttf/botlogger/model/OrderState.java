package org.bttf.botlogger.model;

import lombok.Data;

@Data
public class OrderState {

    /* Example raw JSON:

        lastOrderState:
       { stock: 'Microsoft',
         qty: 50000,
         direction: 'Sell',
         completed: true }

     */

    public String stock;
    public Long qty;
    public String direction;
    public Boolean completed;
    public Double price;
    public String timestamp;
}
