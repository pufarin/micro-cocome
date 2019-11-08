package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderEntry;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ProductDeliveryDuration;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ProductOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ReceivedOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.StockItem;
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
    private final String getStockItemByStoreIdAnsProductId = "/stockitem?storeId={storeId}&productId={productId}";
    private final String updateStockItemAmount = "/stockitem/{stockItemId}";

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

            ResponseEntity<StockItem> stockItemQueryResult =  getStockItem(queryResult.get().getStoreId(), productId);
            if(stockItemQueryResult.getStatusCode().equals(HttpStatus.OK)){
                StockItem stockItem = stockItemQueryResult.getBody();
                int existingAmount = stockItem.getAmount();
                stockItem.setAmount(existingAmount+orderedAmount);
                ResponseEntity<?> updateStockItemAmountRequest = updateStockItemAmount(stockItem,stockItem.getId());
                if(updateStockItemAmountRequest.getStatusCode().equals(HttpStatus.OK)){
                    return new ResponseEntity<>(HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Product amount could not be updated",HttpStatus.INTERNAL_SERVER_ERROR);
                }


            }else{
                return new ResponseEntity<>("Could not retrieve product to be updated",HttpStatus.INTERNAL_SERVER_ERROR);
            }    
                       
        }
    }


    public ResponseEntity<StockItem> getStockItem(long storeId, long productId){
        return webClient.get()
                        .uri(getStockItemByStoreIdAnsProductId, storeId,productId)
                        .exchange()
                        .flatMap(response -> response.toEntity(StockItem.class))
                        .block();
    }


    public ResponseEntity<?> updateStockItemAmount(StockItem stockItem, long stockItemId ){
        return webClient.put().uri(updateStockItemAmount, stockItemId)
                        .syncBody(stockItem)
                        .exchange()
                        .flatMap(response -> response.toEntity(StockItem.class))
                       // .retrieve()
                       // .bodyToMono(ResponseEntity.class)
                        .block();
    }

    public ResponseEntity<List<ProductDeliveryDuration>> getDeliveryDurationPerProduct(List<Long> productsId){
        List<ProductDeliveryDuration> queryResult = productOrderRepository.getNrDaysPerProductDelivery(productsId);
        if(queryResult.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(queryResult);
    }
}
