package net.gabrielkovacs.apigateway.models;

import java.sql.Date;

public class OrderDeliveryDate {

    private Date deliveryDate;

    public OrderDeliveryDate() {}

    public OrderDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


}