package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities;


public class StockItem {

    private long id;
      
    private double salePrice;
    
    private int amount;
  
    private int minStock;
    
    private int maxStock;
  
    private long  productId;

    private long  storeId;

    public StockItem(){
        
    }

    public StockItem(long id, double salePrice, int amount, int minStock, int maxStock, long productId, long storeId) {
        this.id = id;
        this.salePrice = salePrice;
        this.amount = amount;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.productId = productId;
        this.storeId = storeId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMinStock() {
        return this.minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public int getMaxStock() {
        return this.maxStock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }

    public long getProductId() {
        return this.productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getStoreId() {
        return this.storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public StockItem id(long id) {
        this.id = id;
        return this;
    }

    public StockItem salePrice(double salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public StockItem amount(int amount) {
        this.amount = amount;
        return this;
    }

    public StockItem minStock(int minStock) {
        this.minStock = minStock;
        return this;
    }

    public StockItem maxStock(int maxStock) {
        this.maxStock = maxStock;
        return this;
    }

    public StockItem productId(long productId) {
        this.productId = productId;
        return this;
    }

    public StockItem storeId(long storeId) {
        this.storeId = storeId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StockItem)) {
            return false;
        }
        StockItem stockItem = (StockItem) o;
        return id == stockItem.id && salePrice == stockItem.salePrice && amount == stockItem.amount && minStock == stockItem.minStock && maxStock == stockItem.maxStock && productId == stockItem.productId && storeId == stockItem.storeId;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", salePrice='" + getSalePrice() + "'" +
            ", amount='" + getAmount() + "'" +
            ", minStock='" + getMinStock() + "'" +
            ", maxStock='" + getMaxStock() + "'" +
            ", productId='" + getProductId() + "'" +
            ", storeId='" + getStoreId() + "'" +
            "}";
    }


}