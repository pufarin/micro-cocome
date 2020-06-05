package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.*;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderEntryRepository;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.ProductOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

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
