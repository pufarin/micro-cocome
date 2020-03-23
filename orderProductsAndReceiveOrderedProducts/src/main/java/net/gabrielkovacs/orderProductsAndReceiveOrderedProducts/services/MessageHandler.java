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
                QueryResponse queryResponse = new QueryResponse(responsePayload,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse));
                break;
            case("receiveOrder"):
                log.info("Already in the receive order {}", clientCallBack.toString());

                ReceivedOrder receivedOrder = messageManipulation.getReceivedOrderFromJson(clientCallBack.getParameter());

                ResponseEntity<?> responseEntity = orderProductService.updateProductOrderDeliveryDate(receivedOrder,receivedOrder.getOrderId());

                String responsePayloadReceivedOrder = messageManipulation.convertResponseToString(responseEntity);
                QueryResponse queryResponseReceivedOrder = new QueryResponse(responsePayloadReceivedOrder,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponseReceivedOrder));

                break;
        }
    }
}
