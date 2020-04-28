package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderEntry;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ProductOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ServiceBusMessageCommand;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.StockItem;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderEntryRepository;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.ProductOrderRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
public class OrderProductService {

    // use 2 facades for entities and repository?
    private ProductOrderRepository productOrderRepository;
    private OrderEntryRepository orderEntryRepository;
    private MessageManipulation messageManipulation;

    public OrderProductService(ProductOrderRepository productOrderRepository, OrderEntryRepository orderEntryRepository,
                               MessageManipulation messageManipulation){
        this.productOrderRepository = productOrderRepository;
        this.orderEntryRepository = orderEntryRepository;
        this.messageManipulation = messageManipulation;
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

    public List<Long> getDeliveryDurationPerProduct(List<Long> productsId){
        List<Long> queryResult = productOrderRepository.getNrDays(productsId);
        if(queryResult.isEmpty()){
            return Collections.emptyList();
        }

        return queryResult;
    }

    public ServiceBusMessageCommand generateGetStockItemCommandMessage(String correlationId, Long storeId, Long productId,
                                                                       Timestamp timestamp, String sender){
        String command = generateGetStockItemCommand(storeId, productId);
        return new ServiceBusMessageCommand(correlationId, command, timestamp, sender );
    }

    private String generateGetStockItemCommand(Long storeId, Long productId){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "getStockItemByStoreIdAndProductId");
        jsonObject.addProperty("storeId", storeId );
        jsonObject.addProperty("productId", productId);
        return new Gson().toJson(jsonObject);
    }

    public ServiceBusMessageCommand generateUpdateStockItemCommandMessage(String correlationId, StockItem stockItem,
                                                                       Timestamp timestamp, String sender){
        String command = generateUpdateStockItemCommand(stockItem);
        return new ServiceBusMessageCommand(correlationId, command, timestamp, sender );
    }

    private String generateUpdateStockItemCommand(StockItem stockItem){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "updateStockItem");
        jsonObject.addProperty("stockItem", messageManipulation.convertStockItemToString(stockItem) );
        return new Gson().toJson(jsonObject);
    }

}
