package net.gabrielkovacs.apigateway.models;

public class SupplierPerformance {
    
    private Long productSuplierId;
    private Double meanTimeDeliveryInDay;

    public SupplierPerformance(){}

    public SupplierPerformance(Long productSuplierId, Double meanTimeDeliveryInDay) {
        this.productSuplierId = productSuplierId;
        this.meanTimeDeliveryInDay = meanTimeDeliveryInDay;
    }

    public Long getProductSuplierId() {
        return this.productSuplierId;
    }

    public void setProductSuplierId(Long productSuplierId) {
        this.productSuplierId = productSuplierId;
    }

    public Double getMeanTimeDeliveryInDay() {
        return this.meanTimeDeliveryInDay;
    }

    public void setMeanTimeDeliveryInDay(Double meanTimeDeliveryInDay) {
        this.meanTimeDeliveryInDay = meanTimeDeliveryInDay;
    }


}