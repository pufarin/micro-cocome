package net.gabrielkovacs.apigateway.entities;

public class OrderDetails {

    private Long storeId;
    private Long productId;
    private Long amount;

    public OrderDetails(){}

    public OrderDetails(Long storeId, Long productId, Long amount) {
        this.storeId = storeId;
        this.productId = productId;
        this.amount = amount;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "storeId=" + storeId +
                ", productId=" + productId +
                ", amount=" + amount +
                '}';
    }
}
