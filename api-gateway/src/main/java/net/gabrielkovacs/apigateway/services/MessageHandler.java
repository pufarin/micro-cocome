package net.gabrielkovacs.apigateway.services;

import com.google.gson.Gson;
import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import net.gabrielkovacs.apigateway.entities.DeliveryReportsProcessingState;
import net.gabrielkovacs.apigateway.models.QueryResponse;
import net.gabrielkovacs.apigateway.repository.ClientCallBackRepository;
import net.gabrielkovacs.apigateway.repository.DeliveryReportsRepository;
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
    private Gson g = new Gson();

    public MessageHandler(ClientCallBackRepository clientCallBackRepository, ApiGatewayServices apiGatewayServices, MessageManipulation messageManipulation, MessageProducer messageProducer, DeliveryReportsRepository deliveryReportsRepository, DeliveryReportsProcessingStateService deliveryReportsProcessingStateService) {
        this.clientCallBackRepository = clientCallBackRepository;
        this.apiGatewayServices = apiGatewayServices;
        this.messageManipulation = messageManipulation;
        this.messageProducer = messageProducer;
        this.deliveryReportsRepository = deliveryReportsRepository;
        this.deliveryReportsProcessingStateService = deliveryReportsProcessingStateService;
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
        String coorrletionId = queryResponse.getUuid();
        Date date= new Date();
        switch (eventName) {
            case ("generateProductSupplierAndProducts"):
                log.info("Already in the generateProductSupplierAndProducts {}", eventName);
                Optional<DeliveryReportsProcessingState> deliveryReportsProcessingState = deliveryReportsRepository.findById(coorrletionId);

                if(deliveryReportsProcessingState.isPresent()){
                    DeliveryReportsProcessingState deliveryRPS = deliveryReportsProcessingState.get();
                    ClientCallBack clientCallBack = new ClientCallBack(coorrletionId,null,new Timestamp( date.getTime()),
                            "getDeliveryDuration",queryResponse.getPayload());
                    messageProducer.sendMessageToOrderProductsAndReceiveOrderedProducts(apiGatewayServices.generateJSONStringFromClass(clientCallBack));
                    log.info("Sending message to OrderProductsAndReceiveOrderedProducts {}", clientCallBack.toString());
                    String newState = deliveryReportsProcessingStateService.changeState("getDeliveryDuration",
                            deliveryRPS.getDeliveryReportsState());
                    deliveryRPS.setDeliveryReportsState(newState);
                    deliveryReportsRepository.save(deliveryRPS);

                }else{
                    log.info("Could not find Delivery Reports Processing State with correlationId : {} ", coorrletionId);
                }

                break;
            case ("getDeliveryDuration"):
                log.info("Already in the getDeliveryDuration {}", eventName);
                log.info("The Query Response {}", queryResponse.toString());
                Optional<DeliveryReportsProcessingState> deliveryReportsProcessingState1 = deliveryReportsRepository.findById(coorrletionId);
                if(deliveryReportsProcessingState1.isPresent()){
                    DeliveryReportsProcessingState deliveryRPS = deliveryReportsProcessingState1.get();
                    ClientCallBack clientCallBack = new ClientCallBack(coorrletionId,null,new Timestamp( date.getTime()),
                            "generateDeliveryReport",queryResponse.getPayload());
                    messageProducer.sendMessageToShowDeliveryReports(apiGatewayServices.generateJSONStringFromClass(clientCallBack));
                    log.info("Sending message to ShowDeliveryReports {}", clientCallBack.toString());
                    String newState = deliveryReportsProcessingStateService.changeState("generateDeliveryReport",
                            deliveryRPS.getDeliveryReportsState());
                    deliveryRPS.setDeliveryReportsState(newState);
                    deliveryReportsRepository.save(deliveryRPS);

                }else{
                    log.info("Could not find Delivery Reports Processing State with correlationId : {} ", coorrletionId);
                }
                break;

            case ("generateDeliveryReport"):
                log.info("Already in the getDeliveryDuration {}", eventName);
                Optional<DeliveryReportsProcessingState> deliveryReportsProcessingState2 = deliveryReportsRepository.findById(coorrletionId);
                if(deliveryReportsProcessingState2.isPresent()){
                    DeliveryReportsProcessingState deliveryRPS = deliveryReportsProcessingState2.get();
                    String newState = deliveryReportsProcessingStateService.changeState("finished",
                            deliveryRPS.getDeliveryReportsState());
                    deliveryRPS.setDeliveryReportsState(newState);
                    deliveryReportsRepository.save(deliveryRPS);
                }
                replyToClient(coorrletionId,queryResponse);


                break;

        }
    }
}
