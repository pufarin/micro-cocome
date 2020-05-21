package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.*;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.flow.OrderProcessingSubscriber;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderEntryRepository;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderProcessingStateRepository;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.ProductOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.SubmissionPublisher;


import static net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.consumer.OrderState.initiated;

@Service
public class OrderProductService {

    Logger log = LoggerFactory.getLogger(OrderProductService.class);

    private SubmissionPublisher<QueryResponse> publisher = new SubmissionPublisher<>();
    private OrderProcessingSubscriber orderProcessingSubscriber;

    // use 2 facades for entities and repository?
    private ProductOrderRepository productOrderRepository;
    private OrderEntryRepository orderEntryRepository;
    private MessageManipulation messageManipulation;
    private MessageProducer messageProducer;
    private OrderProcessingStateRepository orderProcessingStateRepository;

    public OrderProductService(OrderProcessingSubscriber orderProcessingSubscriber, ProductOrderRepository productOrderRepository, OrderEntryRepository orderEntryRepository,
                               MessageManipulation messageManipulation, MessageProducer messageProducer, OrderProcessingStateRepository orderProcessingStateRepository){
        this.orderProcessingSubscriber = orderProcessingSubscriber;
        this.productOrderRepository = productOrderRepository;
        this.orderEntryRepository = orderEntryRepository;
        this.messageManipulation = messageManipulation;
        this.messageProducer = messageProducer;
        this.orderProcessingStateRepository = orderProcessingStateRepository;
        publisher.subscribe(orderProcessingSubscriber);
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

    public String generateCorrelationId(){
        return UUID.randomUUID().toString();
    }

    public void receiveOrder(Long orderId, String correlationId, ReceivedOrder receivedOrder){
        java.util.Date date= new java.util.Date();
        Timestamp theTimeStamp = new Timestamp( date.getTime());

        Optional<ProductOrder> queryResult = productOrderRepository.getProductOrderByOrderEntryId(orderId);
        if (!queryResult.isPresent()) {
            String responsePayloadReceivedOrder = String.format("The order with the id: %d has not been found", orderId);
            QueryResponse queryResponseReceivedOrder = new QueryResponse("NOT FOUND",correlationId,new Timestamp( date.getTime()));
            //messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponseReceivedOrder));
            publisher.submit(queryResponseReceivedOrder);

        } else {
            ProductOrder productOrder = queryResult.get();
            Long storeId = productOrder.getStoreId();

            //Save delivery date
            productOrder.setDeliveryDate(receivedOrder.getDeliveryDate());
            productOrderRepository.save(productOrder);

            log.info("Everything is OK");
            Optional<OrderEntry> orderEntry1 = orderEntryRepository.findById(orderId);
            long productId = orderEntry1.get().getProductId();
            int orderedAmount = orderEntry1.get().getAmount();

            ServiceBusMessageCommand serviceBusMessageCommand = this.generateGetStockItemCommandMessage(
                    correlationId, storeId , productId, theTimeStamp,"oparop" );

            OrderProcessingState orderProcessingState1 = new OrderProcessingState(correlationId,initiated.name(),
                    "receiveOrder",orderedAmount,productId, storeId);
            orderProcessingStateRepository.save(orderProcessingState1);

            messageProducer.sendRequestStockItem(serviceBusMessageCommand);
        }
    }
}
