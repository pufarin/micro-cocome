package net.gabrielkovacs.common.models;

public class DeliveryReport {

    private long productSuplierId;

    private double meanTimeDeliveryInDay;

    public DeliveryReport(long productSuplierId, double meanTimeDeliveryInDay){
        this.productSuplierId = productSuplierId;
        this.meanTimeDeliveryInDay = meanTimeDeliveryInDay;
    }

    public long getProductSuplierId() {
        return productSuplierId;
    }

    public void setProductSuplierId(long productSuplierId) {
        this.productSuplierId = productSuplierId;
    }



    public double getMeanTimeDeliveryInDay() {
        return meanTimeDeliveryInDay;
    }

    public void setMeanTimeDeliveryInDay(double meanTimeDeliveryInDay) {
        this.meanTimeDeliveryInDay = meanTimeDeliveryInDay;
    }
}
