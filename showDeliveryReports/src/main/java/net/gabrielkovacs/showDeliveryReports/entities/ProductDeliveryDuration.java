package net.gabrielkovacs.showDeliveryReports.entities;

public class ProductDeliveryDuration {

    private long productId;
    private long nrDays;


    public ProductDeliveryDuration() {
    }


    public ProductDeliveryDuration(long productId, long nrDays) {
        this.productId = productId;
        this.nrDays = nrDays;
    }


    public long getProductId() {
        return this.productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getNrDays() {
        return this.nrDays;
    }

    public void setNrDays(long nrDays) {
        this.nrDays = nrDays;
    }



}