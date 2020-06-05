package net.gabrielkovacs.showStockReportsAndChangePrice.entities;

public class OrderDetails {

    private Long storeId;
    private Long productId;
    private int amount;

    public OrderDetails(){}

    public OrderDetails(Long storeId, Long productId, int amount) {
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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
