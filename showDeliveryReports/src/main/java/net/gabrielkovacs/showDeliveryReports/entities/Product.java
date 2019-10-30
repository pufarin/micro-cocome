package net.gabrielkovacs.showDeliveryReports.entities;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.gabrielkovacs.showDeliveryReports.entities.ProductSuplier;

@Entity
@Table(name="product")
public class Product {

    @Id
    @Column(name="id")
    private long id;

    @Column(name="barcode")
    private int barcode;
    @Column(name="name")
    private String name;
    @Column(name="purchaseprice")
    private double purchasePrice;
     
    
    protected Product(){}

    public Product(int barcode, String name, double purchasePrice){
        this.barcode = barcode;
        this.name = name;
        this.purchasePrice = purchasePrice;
    }
/*
    @OneToMany(mappedBy = "product")
    private Collection<StockItem> stockItem;
*/
    @ManyToOne
    @JoinColumn(name = "productsuplierid")
    private ProductSuplier productSuplier;
/*
    @OneToMany(mappedBy = "product")
    private Collection<OrderEntry> orderEntry;
*/
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBarcode() {
        return this.barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPurchasePrice() {
        return this.purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public long getProductSuplierId(){
        return productSuplier.getId();
    }

    public void setProductSuplierId(long id){
        productSuplier.setId(id);
    }

}