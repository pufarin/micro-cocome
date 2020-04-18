package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities;

import java.sql.Date;

public class ReceivedOrder {

    private Date deliveryDate;
        private int storeId;
        private int orderId;

    public ReceivedOrder() {
    }

    public ReceivedOrder(Date deliveryDate, int storeId, int orderId) {
        this.deliveryDate = deliveryDate;
        this.storeId = storeId;
        this.orderId = orderId;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}