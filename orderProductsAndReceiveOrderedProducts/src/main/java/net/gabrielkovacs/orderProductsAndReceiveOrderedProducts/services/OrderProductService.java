package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.common.entities.OrderEntry;
import net.gabrielkovacs.common.entities.ProductOrder;
import net.gabrielkovacs.common.entities.StockItem;
import net.gabrielkovacs.common.repository.OrderEntryRepository;
import net.gabrielkovacs.common.repository.ProductOrderRepository;
import net.gabrielkovacs.common.repository.StockItemRepository;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ReceivedOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

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

    public ResponseEntity<?> updateProductOrderDeliveryDate(ReceivedOrder receivedOrder, long orderId) {
        Optional<ProductOrder> queryResult = productOrderRepository.getProductOrderByOrderEntryId(orderId);
        if (!queryResult.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            queryResult.ifPresent(productOrder -> {
                productOrder.setDeliveryDate(receivedOrder.getDeliveryDate());
                productOrderRepository.save(productOrder);
            });

        

            Optional<OrderEntry> orderEntry = orderEntryRepository.findById(orderId);
            long productId = orderEntry.get().getProductId();
            int orderedAmount = orderEntry.get().getAmount();

            Optional<StockItem> stockItemQueryResult =  getStockItem(queryResult.get().getStoreId(), productId);
            if(stockItemQueryResult.isPresent()){
                StockItem stockItem = stockItemQueryResult.get();
                int existingAmount = stockItem.getAmount();
                stockItem.setAmount(existingAmount+orderedAmount);
                StockItem updateStockItem = stockItemRepository.save(stockItem);

                if(updateStockItem != null){
                    return new ResponseEntity<>(HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Product amount could not be updated",HttpStatus.INTERNAL_SERVER_ERROR);
                }


            }else{
                return new ResponseEntity<>("Could not retrieve product to be updated",HttpStatus.INTERNAL_SERVER_ERROR);
            }    
                       
        }
    }

    private Optional<StockItem> getStockItem(long storeId, long productId){
        return stockItemRepository.findAllByStoreIdAndProductId(storeId,productId);
    }

}
