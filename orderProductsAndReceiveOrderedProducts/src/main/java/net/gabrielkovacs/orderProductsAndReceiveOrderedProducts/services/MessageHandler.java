package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class MessageHandler {
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private MessageManipulation messageManipulation;
    private MessageProducer messageProducer;
    private OrderProductService orderProductService;

    public MessageHandler(MessageManipulation messageManipulation,
                          MessageProducer messageProducer, OrderProductService orderProductService) {
        this.messageManipulation = messageManipulation;
        this.messageProducer = messageProducer;
        this.orderProductService = orderProductService;
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
                QueryResponse queryResponse = new QueryResponse(responsePayload,clientCallBack.getUuid(),new Timestamp( date.getTime()),"orderProduct");
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse));
                break;
            case("updateProductOrderDeliveryDate"):
                log.info("Already in the updateProductOrderDeliveryDate {}", clientCallBack.toString());

                ReceivedOrder receivedOrder = messageManipulation.getReceivedOrderFromJson(clientCallBack.getParameter());

                OrderDetails orderDetails = orderProductService.updateProductOrderDeliveryDate(receivedOrder,receivedOrder.getOrderId());
                String responsePayloadReceivedOrder = messageManipulation.convertOrderDetailsToString(orderDetails );
                QueryResponse queryResponseReceivedOrder = new QueryResponse(responsePayloadReceivedOrder,clientCallBack.getUuid(),new Timestamp( date.getTime()),"updateProductOrderDeliveryDate");
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponseReceivedOrder));

                break;
            case("getDeliveryDuration"):
                log.info("Already in the getDeliveryDuration {}", eventName);

                ProductSupplierAndProducts productSupplierAndProducts = messageManipulation.convertStringToProductSupplierAndProducts(clientCallBack.getParameter());
                ProductSupplierAndProducts duration = orderProductService.getDeliveryDuration(productSupplierAndProducts);

                String responsePayload3 = messageManipulation.convertProductSupplierAndProductsToString(duration);
                QueryResponse queryResponse3 = new QueryResponse(responsePayload3,clientCallBack.getUuid(),new Timestamp( date.getTime()), eventName);
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse3));

                break;
        }
    }
}
