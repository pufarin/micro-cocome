package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities;

import java.util.HashMap;
import java.util.List;

public class ProductSupplierAndProducts {

    public ProductSupplierAndProducts(){}

    private HashMap<Long, List<Long>> supplyChain = new HashMap<Long, List<Long>>();

    public void addEntryToSupplyChain(Long producerId, List<Long> productId){
        this.supplyChain.put(producerId,productId);
    }

    public HashMap<Long, List<Long>> getSupplyChain() {
        return supplyChain;
    }

}
