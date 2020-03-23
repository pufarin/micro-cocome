package net.gabrielkovacs.apigateway.models;

import java.sql.Date;

public class ReceivedOrder {

    private Date deliveryDate;
    private long storeId;
    private long orderId;

    public ReceivedOrder(Date deliveryDate, long storeId, long orderId) {
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

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
