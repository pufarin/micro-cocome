package net.gabrielkovacs.apigateway.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OrderProcessingState {

    @Id
    private String requestUUID;
    private String orderState;
    private String eventName;
    private Long orderId;
    private Long amount;

    public OrderProcessingState(){}

    public OrderProcessingState(String requestUUID, String orderState, String eventName, Long orderId, Long amount) {
        this.requestUUID = requestUUID;
        this.orderState = orderState;
        this.eventName = eventName;
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long productId) {
        this.orderId = productId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderProcessingState{" +
                "requestUUID='" + requestUUID + '\'' +
                ", orderState='" + orderState + '\'' +
                ", eventName='" + eventName + '\'' +
                ", orderId=" + orderId +
                ", amount=" + amount +
                '}';
    }
}
