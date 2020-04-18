package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.*;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderEntryRepository;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderProcessingStateRepository;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.ProductOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.consumer.OrderState.*;

@Service
public class MessageHandler {
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private MessageManipulation messageManipulation;
    private MessageProducer messageProducer;
    private OrderProductService orderProductService;
    private OrderProcessingStateRepository orderProcessingStateRepository;
    private OrderEntryRepository orderEntryRepository;
    private ProductOrderRepository productOrderRepository;
    private OrderProcessingStateService orderProcessingStateService;


    public MessageHandler(MessageManipulation messageManipulation,
                          MessageProducer messageProducer, OrderProductService orderProductService,
                          OrderProcessingStateRepository orderProcessingStateRepository,
                          OrderEntryRepository orderEntryRepository,
                          ProductOrderRepository productOrderRepository,
                          OrderProcessingStateService orderProcessingStateService) {
        this.messageManipulation = messageManipulation;
        this.messageProducer = messageProducer;
        this.orderProductService = orderProductService;
        this.orderProcessingStateRepository = orderProcessingStateRepository;
        this.orderEntryRepository = orderEntryRepository;
        this.productOrderRepository = productOrderRepository;
        this.orderProcessingStateService = orderProcessingStateService;
    }

    public void cosumeMessage(ClientCallBack clientCallBack){
        log.info("I am in the handler,  {}", clientCallBack.toString());
        String eventName = clientCallBack.getEventName();
        Date date= new Date();
        switch (eventName){
            case("orderProduct"):
                log.info("Already in the orderProduct {}", eventName);

                IncomingProductOrder incomingProductOrder = messageManipulation.getIncomingProductOrderFromJSON(
                                                            clientCallBack.getParameter());
                OrderEntry orderEntry =  orderProductService.orderProduct(new OrderEntry(incomingProductOrder.getAmount(),
                        incomingProductOrder.getProductId()),incomingProductOrder.getStoreId());
                String responsePayload = messageManipulation.convertOrderEntryToString(orderEntry);
                QueryResponse queryResponse = new QueryResponse(responsePayload,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse));
                //new code
                OrderProcessingState orderProcessingState = new OrderProcessingState(clientCallBack.getUuid(),initiated.name(),
                        eventName,incomingProductOrder.getAmount(), incomingProductOrder.getProductId(),incomingProductOrder.getStoreId());
                orderProcessingStateRepository.save(orderProcessingState);
                break;
            case("receiveOrder"):
                log.info("Already in the receive order {}", clientCallBack.toString());
                Timestamp theTimeStamp = new Timestamp( date.getTime());

                // The incoming data
                ReceivedOrder receivedOrder = messageManipulation.getReceivedOrderFromJson(clientCallBack.getParameter());
                log.info("This IS THE ORDER ID: {}", receivedOrder.getOrderId());

                Optional<ProductOrder> queryResult = productOrderRepository.getProductOrderByOrderEntryId(receivedOrder.getOrderId());
                if (!queryResult.isPresent()) {
                    String responsePayloadReceivedOrder = String.format("The order with the id: %d has not been found", receivedOrder.getOrderId());
                    QueryResponse queryResponseReceivedOrder = new QueryResponse(responsePayloadReceivedOrder,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                    messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponseReceivedOrder));

                } else {
                    log.info("Everything is OK");
                    Optional<OrderEntry> orderEntry1 = orderEntryRepository.findById((long) receivedOrder.getOrderId());
                    long productId = orderEntry1.get().getProductId();
                    int orderedAmount = orderEntry1.get().getAmount();

                    ServiceBusMessageCommand serviceBusMessageCommand = orderProductService.generateGetStockItemCommandMessage(
                            clientCallBack.getUuid(), (long) receivedOrder.getStoreId(), productId, theTimeStamp,"oparop" );

                    OrderProcessingState orderProcessingState1 = new OrderProcessingState(clientCallBack.getUuid(),initiated.name(),
                            eventName,orderedAmount,productId,(long) receivedOrder.getStoreId());
                    orderProcessingStateRepository.save(orderProcessingState1);

                    messageProducer.sendRequestStockItem(serviceBusMessageCommand);
                }
                // Get the product Id so that the command message can be created


                /*
                ServiceBusMessageCommand serviceBusMessageCommand = orderProductService.generateCommandMessage(
                        clientCallBack.getUuid(), (long) receivedOrder.getStoreId(), productId, theTimeStamp,"oparop" );

                // Save the state of the order
                OrderProcessingState orderProcessingState1 = new OrderProcessingState(clientCallBack.getUuid(),initiated.name(),
                        eventName,orderedAmount,productId,(long) receivedOrder.getStoreId());
                orderProcessingStateRepository.save(orderProcessingState1);

                messageProducer.sendRequestStockItem(serviceBusMessageCommand);

                log.info("This is the message command {}", serviceBusMessageCommand);


                ResponseEntity<?> responseEntity = orderProductService.updateProductOrderDeliveryDate(receivedOrder,receivedOrder.getOrderId());

               String responsePayloadReceivedOrder = messageManipulation.convertResponseToString(responseEntity);
                QueryResponse queryResponseReceivedOrder = new QueryResponse(responsePayloadReceivedOrder,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponseReceivedOrder));
              */
                break;
        }
    }

    public void consumeServiceBusMessageCommand(ServiceBusMessageCommand serviceBusMessageCommand){
        log.info("I am in the ServiceBusMessageCommand handler,  {}", serviceBusMessageCommand.toString());
        String commandData = serviceBusMessageCommand.getCommand();
        JsonObject command = new Gson().fromJson(commandData, JsonObject.class);
        String methodName = command.get("method").getAsString();
        Date date= new Date();
        switch (methodName){
            case("getDeliveryDurationPerProduct"):
                log.info("This is the getDeliveryDurationPerProduct CASE");
                Timestamp theTimeStamp = new Timestamp( date.getTime());

                // https://stackoverflow.com/questions/18544133/parsing-json-array-into-java-util-list-with-gson
                JsonArray jsonArray = command.getAsJsonArray("productId");
                List<Long> productId = new Gson().fromJson(jsonArray, ArrayList.class);

                List<ProductDeliveryDuration> productDeliveryDurations = orderProductService.getDeliveryDurationPerProduct(productId);
                String document = messageManipulation.convertProductDeliveryDurations(productDeliveryDurations);
                ServiceBusMessageResponse serviceBusMessageResponse = new ServiceBusMessageResponse(serviceBusMessageCommand.getCorrelationId(),
                        document,theTimeStamp,"oparop", "getDeliveryDurationPerProduct");

                messageProducer.sendMessageToServiceBusResponse(messageManipulation.convertServiceBusMessageResponseToString(serviceBusMessageResponse));
                break;
        }

    }

    public void consumeServiceBusMessageResponse(ServiceBusMessageResponse serviceBusMessageResponse){
        log.info("I am in the ServiceBusMessageResponse handler,  {}", serviceBusMessageResponse.toString());
        String eventName = serviceBusMessageResponse.getEventName();
        Date date= new Date();
        switch (eventName) {
            case("request_item_stock"):
                log.info("I am in the request_item_stock CASE");
                Timestamp theTimeStamp = new Timestamp( date.getTime());
                StockItem stockItem = messageManipulation.convertStringToStockItem(serviceBusMessageResponse.getDocument());
                Optional<OrderProcessingState> orderProcessingStateQuery = orderProcessingStateRepository.findById(serviceBusMessageResponse.getCorrelationId());
                if(orderProcessingStateQuery.isPresent()){
                    OrderProcessingState orderProcessingState = orderProcessingStateQuery.get();
                    String currentState = orderProcessingState.getOrderState();
                    int currentAmount = stockItem.getAmount();
                    int orderedAmount = orderProcessingState.getAmount();
                    orderProcessingState.setAmount(currentAmount+orderedAmount);
                    orderProcessingStateRepository.save(orderProcessingState);

                    String newState = orderProcessingStateService.changeState(computed_new_stock.name(),currentState);
                    orderProcessingState.setOrderState(newState);
                    orderProcessingStateRepository.save(orderProcessingState);

                    stockItem.setAmount(currentAmount+orderedAmount);
                    ServiceBusMessageCommand serviceBusMessageCommand = orderProductService.generateUpdateStockItemCommandMessage(
                            orderProcessingState.getRequestUUID(),stockItem, theTimeStamp, "oparop");
                    log.info("This is the serviceBusMessageCommand in the request item stock: {}", serviceBusMessageCommand.toString());
                    messageProducer.sendUpdateStockItem(serviceBusMessageCommand);


                }else{
                    log.info("The the order with the correlationId:{} could not be found", serviceBusMessageResponse.getCorrelationId());
                }
            case("updated_stock"):
                Timestamp theTimeStamp1 = new Timestamp( date.getTime());
                StockItem stockItem1 = messageManipulation.convertStringToStockItem(serviceBusMessageResponse.getDocument());
                Optional<OrderProcessingState> orderProcessingStateQuery1 = orderProcessingStateRepository.findById(serviceBusMessageResponse.getCorrelationId());
                if(orderProcessingStateQuery1.isPresent()){
                    OrderProcessingState orderProcessingState = orderProcessingStateQuery1.get();
                    String currentState = orderProcessingState.getOrderState();

                    String newState = orderProcessingStateService.changeState(updated_stock.name(),currentState);
                    orderProcessingState.setOrderState(newState);
                    orderProcessingStateRepository.save(orderProcessingState);

                    if(newState.equals(finished_update.name())){
                        QueryResponse queryResponseReceivedOrder = new QueryResponse(serviceBusMessageResponse.getDocument(),orderProcessingState.getRequestUUID(), theTimeStamp1);
                        messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponseReceivedOrder));
                    }



                }else{
                    log.info("The the order with the correlationId:{} could not be found", serviceBusMessageResponse.getCorrelationId());
                }





        }
    }


}
