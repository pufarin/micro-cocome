package net.gabrielkovacs.apigateway.models;

public class SubmitedOrder {

    private Long amount;
    private Long productId;


    public SubmitedOrder() {
    }


    public SubmitedOrder(Long amount, Long productId) {
        this.amount = amount;
        this.productId = productId;
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

}