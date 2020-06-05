package net.gabrielkovacs.apigateway.services;

import com.google.gson.Gson;
import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import net.gabrielkovacs.apigateway.entities.DeliveryReportsProcessingState;
import net.gabrielkovacs.apigateway.entities.OrderDetails;
import net.gabrielkovacs.apigateway.entities.OrderProcessingState;
import net.gabrielkovacs.apigateway.models.QueryResponse;
import net.gabrielkovacs.apigateway.models.StockItem;
import net.gabrielkovacs.apigateway.repository.ClientCallBackRepository;
import net.gabrielkovacs.apigateway.repository.DeliveryReportsRepository;
import net.gabrielkovacs.apigateway.repository.OrderProcessingStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
public class MessageHandler {

    Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private ClientCallBackRepository clientCallBackRepository;
    private ApiGatewayServices apiGatewayServices;
    private MessageManipulation messageManipulation;
    private MessageProducer messageProducer;
    private DeliveryReportsRepository deliveryReportsRepository;
    private DeliveryReportsProcessingStateService deliveryReportsProcessingStateService;
    private OrderProcessingStateRepository orderProcessingStateRepository;
    private OrderProcessingStateService orderProcessingStateService;
    private Gson g = new Gson();

    public MessageHandler(ClientCallBackRepository clientCallBackRepository, ApiGatewayServices apiGatewayServices, MessageManipulation messageManipulation, MessageProducer messageProducer, DeliveryReportsRepository deliveryReportsRepository, DeliveryReportsProcessingStateService deliveryReportsProcessingStateService, OrderProcessingStateRepository orderProcessingStateRepository, OrderProcessingStateService orderProcessingStateService) {
        this.clientCallBackRepository = clientCallBackRepository;
        this.apiGatewayServices = apiGatewayServices;
        this.messageManipulation = messageManipulation;
        this.messageProducer = messageProducer;
        this.deliveryReportsRepository = deliveryReportsRepository;
        this.deliveryReportsProcessingStateService = deliveryReportsProcessingStateService;
        this.orderProcessingStateRepository = orderProcessingStateRepository;
        this.orderProcessingStateService = orderProcessingStateService;
    }

    public void replyToClient(String correlationId, QueryResponse queryResponse){
        getClientCallbackById(correlationId).ifPresentOrElse(data -> {
            ClientCallBack clientCallBack = new ClientCallBack(data.getUuid(), data.getCall_back(),
                    data.getTimeStamp(), data.getEventName(), queryResponse.getPayload());

            sendDataToCallback(data.getCall_back(), clientCallBack);
        }, () -> log.info("The desired UUID has not been found in the DB"));

    }


    public ResponseEntity<ClientCallBack> sendDataToCallback(String callbackUrl, ClientCallBack clientCallBack ){
       return apiGatewayServices.sendDataToCallbackAddress(callbackUrl, clientCallBack);
    }

    public QueryResponse convertStringToQueryResponse(String message){
        return g.fromJson(message, QueryResponse.class);
    }

    public Optional<ClientCallBack> getClientCallbackById(String uuid){
        return  clientCallBackRepository.findById(uuid);
    }

    public void cosumeMessage(QueryResponse queryResponse){
        log.info("I am in the handler,  {}", queryResponse.toString());
        String eventName = queryResponse.getMessageType();
        String correlationId = queryResponse.getUuid();
        Date date= new Date();
        switch (eventName) {
            case ("generateProductSupplierAndProducts"):
                log.info("Already in the generateProductSupplierAndProducts {}", eventName);
                Optional<DeliveryReportsProcessingState> deliveryReportsProcessingState = deliveryReportsRepository.findById(correlationId);

                if(deliveryReportsProcessingState.isPresent()){
                    DeliveryReportsProcessingState deliveryRPS = deliveryReportsProcessingState.get();
                    ClientCallBack clientCallBack = new ClientCallBack(correlationId,null,new Timestamp( date.getTime()),
                            "getDeliveryDuration",queryResponse.getPayload());
                    messageProducer.sendMessageToOrderProductsAndReceiveOrderedProducts(apiGatewayServices.generateJSONStringFromClass(clientCallBack));
                    log.info("Sending message to OrderProductsAndReceiveOrderedProducts {}", clientCallBack.toString());
                    String newState = deliveryReportsProcessingStateService.changeState("getDeliveryDuration",
                            deliveryRPS.getDeliveryReportsState());
                    deliveryRPS.setDeliveryReportsState(newState);
                    deliveryReportsRepository.save(deliveryRPS);

                }else{
                    log.info("Could not find Delivery Reports Processing State with correlationId : {} ", correlationId);
                }

                break;
            case ("getDeliveryDuration"):
                log.info("Already in the getDeliveryDuration {}", eventName);
                log.info("The Query Response {}", queryResponse.toString());
                Optional<DeliveryReportsProcessingState> deliveryReportsProcessingState1 = deliveryReportsRepository.findById(correlationId);
                if(deliveryReportsProcessingState1.isPresent()){
                    DeliveryReportsProcessingState deliveryRPS = deliveryReportsProcessingState1.get();
                    ClientCallBack clientCallBack = new ClientCallBack(correlationId,null,new Timestamp( date.getTime()),
                            "generateDeliveryReport",queryResponse.getPayload());
                    messageProducer.sendMessageToShowDeliveryReports(apiGatewayServices.generateJSONStringFromClass(clientCallBack));
                    log.info("Sending message to ShowDeliveryReports {}", clientCallBack.toString());
                    String newState = deliveryReportsProcessingStateService.changeState("generateDeliveryReport",
                            deliveryRPS.getDeliveryReportsState());
                    deliveryRPS.setDeliveryReportsState(newState);
                    deliveryReportsRepository.save(deliveryRPS);

                }else{
                    log.info("Could not find Delivery Reports Processing State with correlationId : {} ", correlationId);
                }
                break;

            case ("generateDeliveryReport"):
                log.info("Already in the getDeliveryDuration {}", eventName);
                Optional<DeliveryReportsProcessingState> deliveryReportsProcessingState2 = deliveryReportsRepository.findById(correlationId);
                if(deliveryReportsProcessingState2.isPresent()){
                    DeliveryReportsProcessingState deliveryRPS = deliveryReportsProcessingState2.get();
                    String newState = deliveryReportsProcessingStateService.changeState("finished",
                            deliveryRPS.getDeliveryReportsState());
                    deliveryRPS.setDeliveryReportsState(newState);
                    deliveryReportsRepository.save(deliveryRPS);
                }
                replyToClient(correlationId,queryResponse);
                break;
            case("updateProductOrderDeliveryDate"):
                Optional<OrderProcessingState> orderProcessingState = orderProcessingStateRepository.findById(correlationId);
                if(orderProcessingState.isPresent()) {
                    OrderProcessingState ops = orderProcessingState.get();
                    OrderDetails orderDetails = messageManipulation.convertStringToOrderDetails(queryResponse.getPayload());
                    ClientCallBack clientCallBack = new ClientCallBack(correlationId,null,new Timestamp( date.getTime()),
                            "request_item_stock",queryResponse.getPayload());
                    messageProducer.sendMessageToShowStockReports(apiGatewayServices.generateJSONStringFromClass(clientCallBack));

                    String newState = orderProcessingStateService.changeState("request_item_stock",
                            ops.getOrderState());
                    ops.setOrderState(newState);
                    ops.setAmount(orderDetails.getAmount() );
                    orderProcessingStateRepository.save(ops);


                }else{
                    log.info("Could not find Order Processing State with correlationId : {} ", correlationId);
                }

                break;
            case("request_item_stock"):
                Optional<OrderProcessingState> orderProcessingState1 = orderProcessingStateRepository.findById(correlationId);
                if(orderProcessingState1.isPresent()) {
                    OrderProcessingState ops = orderProcessingState1.get();
                    StockItem stockItem = messageManipulation.convertStringToStockItem(queryResponse.getPayload());
                    Long currentAmount = stockItem.getAmount();
                    stockItem.setAmount(currentAmount + ops.getAmount());
                    String payload = messageManipulation.convertStockItemToString(stockItem);
                    ClientCallBack clientCallBack = new ClientCallBack(correlationId,null,new Timestamp( date.getTime()),
                            "updated_stock",payload);
                    messageProducer.sendMessageToShowStockReports(apiGatewayServices.generateJSONStringFromClass(clientCallBack));

                    String newState = orderProcessingStateService.changeState("updated_stock",
                            ops.getOrderState());
                    ops.setOrderState(newState);
                    orderProcessingStateRepository.save(ops);


                }else{
                    log.info("Could not find Order Processing State with correlationId : {} ", correlationId);
                }
                break;
            case("updated_stock"):
                log.info("Already in the getDeliveryDuration {}", eventName);
                Optional<OrderProcessingState> orderProcessingState2 = orderProcessingStateRepository.findById(correlationId);
                if(orderProcessingState2.isPresent()){
                    OrderProcessingState ops = orderProcessingState2.get();
                    String newState = orderProcessingStateService.changeState("finished_update",
                            ops.getOrderState());
                    ops.setOrderState(newState);
                    orderProcessingStateRepository.save(ops);

                }
                replyToClient(correlationId,queryResponse);
                break;
            case("orderProduct"):
                replyToClient(correlationId,queryResponse);
                break;
            case("returnStockItemReports"):
                replyToClient(correlationId,queryResponse);
                break;
            case("changeStockItemPrice"):
                replyToClient(correlationId,queryResponse);
                break;
        }
    }
}
