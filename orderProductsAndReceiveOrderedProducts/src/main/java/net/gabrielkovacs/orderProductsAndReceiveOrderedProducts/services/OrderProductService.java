package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.*;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderEntryRepository;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.ProductOrderRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderProductService {

    // use 2 facades for entities and repository?
    private ProductOrderRepository productOrderRepository;
    private OrderEntryRepository orderEntryRepository;


    public OrderProductService(ProductOrderRepository productOrderRepository, OrderEntryRepository orderEntryRepository){
        this.productOrderRepository = productOrderRepository;
        this.orderEntryRepository = orderEntryRepository;
    }

    public OrderEntry orderProduct(OrderEntry orderEntry, long storeId){

        orderEntry.setProductOrder(saveProductOrder(storeId));
        return orderEntryRepository.save(orderEntry);
    }

    private ProductOrder saveProductOrder(long storeId) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setOrderingDate(new Date(System.currentTimeMillis()));
        productOrder.setStoreId(storeId);
        productOrder = productOrderRepository.save(productOrder);
        return productOrder;
    }

    public OrderDetails updateProductOrderDeliveryDate(ReceivedOrder receivedOrder, long orderId) {
        Optional<ProductOrder> queryResult = productOrderRepository.getProductOrderByOrderEntryId(orderId);
        if (!queryResult.isPresent()) {
            return new OrderDetails();
        } else {
            ProductOrder productOrder = queryResult.get();
            productOrder.setDeliveryDate(receivedOrder.getDeliveryDate());
            productOrderRepository.save(productOrder);

            Optional<OrderEntry> orderEntry = orderEntryRepository.findById(orderId);
            long productId = orderEntry.get().getProductId();
            int orderedAmount = orderEntry.get().getAmount();

            return new OrderDetails(productOrder.getStoreId(),productId, orderedAmount);

        }
    }


    public ProductSupplierAndProducts getDeliveryDuration(ProductSupplierAndProducts productSupplierAndProducts){
        HashMap<Long, List<Long>> supplyChain = productSupplierAndProducts.getSupplyChain();

        ProductSupplierAndProducts productDeliveryDurations = new ProductSupplierAndProducts();

        List<Long> productSuppliers = supplyChain.keySet().stream().collect(Collectors.toList());

        for(Long supplier: productSuppliers){
            List<Long> nrDays = getDeliveryDurationPerProduct(supplyChain.get(supplier));
            productDeliveryDurations.addEntryToSupplyChain(supplier, nrDays);
        }

        return productDeliveryDurations;
    }


    private List<Long> getDeliveryDurationPerProduct(List<Long> productsId){
        List<Long> queryResult = productOrderRepository.getNrDays(productsId);
        if(queryResult.isEmpty()){
            return Collections.emptyList();
        }
        return queryResult;
    }
}
