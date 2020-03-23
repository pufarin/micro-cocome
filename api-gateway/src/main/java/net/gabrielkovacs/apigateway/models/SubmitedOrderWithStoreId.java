package net.gabrielkovacs.apigateway.models;

public class SubmitedOrderWithStoreId {

    private Long amount;
    private Long productId;
    private Long storeId;


    public SubmitedOrderWithStoreId() {
    }


    public SubmitedOrderWithStoreId(Long amount, Long productId, Long storeId) {
        this.amount = amount;
        this.productId = productId;
        this.storeId = storeId;
    }


    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

}