package net.gabrielkovacs.apigateway.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DeliveryReportsProcessingState {

    @Id
    private String requestUUID;
    private String deliveryReportsState;
    private String eventName;
    private String parameter;

    public DeliveryReportsProcessingState() {
    }

    public DeliveryReportsProcessingState(String requestUUID, String deliveryReportsState, String eventName, String parameter) {
        this.requestUUID = requestUUID;
        this.deliveryReportsState = deliveryReportsState;
        this.eventName = eventName;
        this.parameter = parameter;
    }

    public String getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }

    public String getDeliveryReportsState() {
        return deliveryReportsState;
    }

    public void setDeliveryReportsState(String deliveryReportsState) {
        this.deliveryReportsState = deliveryReportsState;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return "OrderProcessingState{" +
                "requestUUID='" + requestUUID + '\'' +
                ", deliveryReportsState='" + deliveryReportsState + '\'' +
                ", eventName='" + eventName + '\'' +
                ", parameter='" + parameter + '\'' +
                '}';
    }
}
