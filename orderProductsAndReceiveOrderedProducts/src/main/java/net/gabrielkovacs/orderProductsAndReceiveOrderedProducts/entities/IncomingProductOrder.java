package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities;

public class IncomingProductOrder {

    private Integer amount;
    private Long productId;
    private Long storeId;


    public IncomingProductOrder() {
    }


    public IncomingProductOrder(Integer amount, Long productId, Long storeId) {
        this.amount = amount;
        this.productId = productId;
        this.storeId = storeId;
    }


    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
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