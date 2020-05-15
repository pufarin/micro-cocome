package net.gabrielkovacs.common.entities;


import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="tradingenterprise")
public class TradingEnterprise {

    @Id
    @Column(name="id")
    private long id;

    @Column(name="name")
    private String name;

    @ManyToMany
    @JoinTable(name = "tradingE_productS",
            joinColumns = @JoinColumn(name = "tradingEnterprise_id"),
            inverseJoinColumns = @JoinColumn(name = "productSupplier_id")
    )
    private Collection<ProductSuplier> productSupliers;
/*
    @OneToMany(mappedBy = "tradingEnterprise")
    private Collection<Store> stores;
*/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
