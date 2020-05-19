package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities;

import java.sql.Date;

public class ReceivedOrder {

    private Date deliveryDate;
        private Long storeId;
        private Long orderId;

    public ReceivedOrder() {
    }

    public ReceivedOrder(Date deliveryDate, Long storeId, Long orderId) {
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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}