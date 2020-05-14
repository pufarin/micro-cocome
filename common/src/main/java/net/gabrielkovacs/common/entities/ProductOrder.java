package net.gabrielkovacs.common.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;


@Entity
@Table(name="productorder")
public class ProductOrder{

    @Id
    @Column(name="id")
    @SequenceGenerator(name = "mySeqGen", sequenceName = "mySeq1", initialValue = 12, allocationSize = 100)
    @GeneratedValue(generator = "mySeqGen")
    private long id;
    
    private Date deliveryDate;
    private Date orderingDate;
    private long storeId;

    @OneToMany(mappedBy = "productOrder")
    private Collection<OrderEntry> orderEntry;

    public ProductOrder(){}

    public ProductOrder(Date deliveryDate, Date orderingDate, long storeId) {
        this.deliveryDate = deliveryDate;
        this.orderingDate = orderingDate;
        this.storeId = storeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getOrderingDate() {
        return orderingDate;
    }

    public void setOrderingDate(Date orderingDate) {
        this.orderingDate = orderingDate;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
}