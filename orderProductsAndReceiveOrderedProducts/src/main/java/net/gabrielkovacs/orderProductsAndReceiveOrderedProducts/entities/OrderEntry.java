package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities;



import javax.persistence.*;


@Entity
@Table(name="orderentry")
public class OrderEntry{

    @Id
    @Column(name="id")
    @SequenceGenerator(name = "mySeqGen", sequenceName = "mySeq", initialValue = 113, allocationSize = 100)
    @GeneratedValue(generator = "mySeqGen")
    private long id;

    @Column(name="amount")
    private int amount;

    private long productId;

    @ManyToOne
    // @JoinColumn(name = "productorderid")
    private ProductOrder productOrder;

    public OrderEntry(){}

    public OrderEntry(int amount, long productId) {
        this.amount = amount;
        this.productId = productId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProductOrderId() {
        return productOrder.getId();
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }



}