package net.gabrielkovacs.showStockReportsAndChangePrice.entities;

import lombok.ToString;

import javax.persistence.*;

@ToString
@Entity
@Table(name="stockitem")
public
class StockItem {

    public StockItem() {
    }

    public StockItem(long id, double salePrice, int amount, int minStock, int maxStock, long productId, long storeId){
        this.id = id;
        this.salePrice = salePrice;
        this.amount = amount;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.productId = productId;
        this.storeId = storeId;

    }

    @Id
    @Column(name="id")
    private long id;
    
    @Column(name="saleprice")
    private double salePrice;
    @Column(name="amount")
    private int amount;
    @Column(name="minstock")
    private int minStock;
    @Column(name="maxstock")
    private int maxStock;
    @Column(name="productid")
    private long  productId;
    @Column(name="storeid")
    private long  storeId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
}