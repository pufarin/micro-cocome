package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.common.entities.OrderEntry;
import net.gabrielkovacs.common.entities.ProductOrder;
import net.gabrielkovacs.common.entities.StockItem;
import net.gabrielkovacs.common.models.ProductSupplierAndProducts;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderDetails;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ReceivedOrder;
import net.gabrielkovacs.common.repository.OrderEntryRepository;
import net.gabrielkovacs.common.repository.ProductOrderRepository;
import net.gabrielkovacs.common.repository.StockItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderProductService {

    // use 2 facades for entities and repository?
    private ProductOrderRepository productOrderRepository;
    private OrderEntryRepository orderEntryRepository;
    private StockItemRepository stockItemRepository;

    public OrderProductService(ProductOrderRepository productOrderRepository, OrderEntryRepository orderEntryRepository, StockItemRepository stockItemRepository){
        this.productOrderRepository = productOrderRepository;
        this.orderEntryRepository = orderEntryRepository;
        this.stockItemRepository = stockItemRepository;
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

    public ResponseEntity<OrderDetails> updateProductOrderDeliveryDate(ReceivedOrder receivedOrder, long orderId) {
        Optional<ProductOrder> queryResult = productOrderRepository.getProductOrderByOrderEntryId(orderId);
        if (!queryResult.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            ProductOrder productOrder = queryResult.get();
            productOrder.setDeliveryDate(receivedOrder.getDeliveryDate());
            productOrderRepository.save(productOrder);

            Optional<OrderEntry> orderEntry = orderEntryRepository.findById(orderId);
            long productId = orderEntry.get().getProductId();
            int orderedAmount = orderEntry.get().getAmount();

            return ResponseEntity.ok().body(new OrderDetails(productOrder.getStoreId(),productId, orderedAmount));

        }
    }

    public ResponseEntity<ProductSupplierAndProducts> getDeliveryDuration(ProductSupplierAndProducts productSupplierAndProducts){
        HashMap<Long, List<Long>> supplyChain = productSupplierAndProducts.getSupplyChain();

        ProductSupplierAndProducts productDeliveryDurations = new ProductSupplierAndProducts();

        List<Long> productSuppliers = supplyChain.keySet().stream().collect(Collectors.toList());

        for(Long supplier: productSuppliers){
            List<Long> nrDays = getDeliveryDurationPerProduct(supplyChain.get(supplier));
            productDeliveryDurations.addEntryToSupplyChain(supplier, nrDays);
        }

        return ResponseEntity.ok().body(productDeliveryDurations);
    }

    private List<Long> getDeliveryDurationPerProduct(List<Long> productsId){
        List<Long> queryResult = productOrderRepository.getNrDays(productsId);
        if(queryResult.isEmpty()){
            return Collections.emptyList();
        }
        return queryResult;
    }


}
