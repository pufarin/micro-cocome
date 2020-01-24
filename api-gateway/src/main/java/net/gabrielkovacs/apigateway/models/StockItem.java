package net.gabrielkovacs.apigateway.models;


public class StockItem{

    private Long id;
    private Long amount;
    private Long maxStock;
    private Long minStock;
    private Double salePrice;
    private Long product;
    private Long store;
    
    public StockItem(){}

    public StockItem(Long id, Long amount, Long maxStock, Long minStock, Double salePrice, Long product, Long store) {
        this.id = id;
        this.amount = amount;
        this.maxStock = maxStock;
        this.minStock = minStock;
        this.salePrice = salePrice;
        this.product = product;
        this.store = store;
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

    public Long getMaxStock() {
        return this.maxStock;
    }

    public void setMaxStock(Long maxStock) {
        this.maxStock = maxStock;
    }

    public Long getMinStock() {
        return this.minStock;
    }

    public void setMinStock(Long minStock) {
        this.minStock = minStock;
    }

    public Double getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Long getProduct() {
        return this.product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public Long getStore() {
        return this.store;
    }

    public void setStore(Long store) {
        this.store = store;
    }

}