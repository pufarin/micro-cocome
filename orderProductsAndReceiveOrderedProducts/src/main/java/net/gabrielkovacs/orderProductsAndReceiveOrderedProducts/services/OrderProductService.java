package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.*;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderEntryRepository;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.ProductOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderProductService {

    // use 2 facades for entities and repository?
    private ProductOrderRepository productOrderRepository;
    private OrderEntryRepository orderEntryRepository;
    
    private final String baseUri = "http://localhost:8085";

    private WebClient webClient = WebClient.create(baseUri);

    public OrderProductService(ProductOrderRepository productOrderRepository, OrderEntryRepository orderEntryRepository){
        this.productOrderRepository = productOrderRepository;
        this.orderEntryRepository = orderEntryRepository;
    }

    public void setWebClientBaseUri(String baseUri){
        this.webClient = WebClient.create(baseUri);
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



    public ResponseEntity<List<ProductDeliveryDuration>> getDeliveryDurationPerProduct(List<Long> productsId){
        List<ProductDeliveryDuration> queryResult = productOrderRepository.getNrDaysPerProductDelivery(productsId);
        if(queryResult.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(queryResult);
    }
}
