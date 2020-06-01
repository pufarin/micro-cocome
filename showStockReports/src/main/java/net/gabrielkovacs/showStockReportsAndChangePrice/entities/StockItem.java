package net.gabrielkovacs.showStockReportsAndChangePrice.entities;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name="stockitem")
public
class StockItem {

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



}