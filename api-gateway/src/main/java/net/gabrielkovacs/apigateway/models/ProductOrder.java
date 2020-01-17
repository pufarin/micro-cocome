package net.gabrielkovacs.apigateway.models;

public class ProductOrder {
    
    private Long id;
    private Long amount;
    private Long productId;
    private Long productOrderId;

    public ProductOrder() {
    }


    public ProductOrder(Long id, Long amount, Long productId, Long productOrderId) {
        this.id = id;
        this.amount = amount;
        this.productId = productId;
        this.productOrderId = productOrderId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getProductOrderId() {
        return this.productOrderId;
    }

    public void setProductOrderId(Long productOrderId) {
        this.productOrderId = productOrderId;
    }

}