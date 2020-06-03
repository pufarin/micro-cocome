package net.gabrielkovacs.apigateway.consumer;

import net.gabrielkovacs.apigateway.models.QueryResponse;
import net.gabrielkovacs.apigateway.services.MessageHandler;
import net.gabrielkovacs.apigateway.services.MessageManipulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    Logger log = LoggerFactory.getLogger(Consumer.class);
    private MessageHandler messageHandler;
    private MessageManipulation messageManipulation;

    public Consumer(MessageHandler messageHandler, MessageManipulation messageManipulation) {
        this.messageHandler = messageHandler;
        this.messageManipulation = messageManipulation;
    }

/*
    @JmsListener(destination = "api_gateway")
    public void consume(String message){
        log.info("Received Message in API GATEWAY: {}", message);
        QueryResponse incomingMessage = messageHandler.convertStringToQueryResponse(message);
        messageHandler.getClientCallbackById(incomingMessage.getUuid()).ifPresentOrElse(data -> {
            ClientCallBack clientCallBack = new ClientCallBack(data.getUuid(), data.getCall_back(),
                    data.getTimeStamp(), data.getEventName(), incomingMessage.getPayload());

            messageHandler.sendDataToCallback(data.getCall_back(), clientCallBack);
        }, () -> log.info("The desired UUID has not been found in the DB"));

    }
*/

    @JmsListener(destination = "api_gateway")
    public void consume(String message){
        log.info("Received Message in API GATEWAY: {}", message);
        QueryResponse incomingMessage = messageManipulation.convertStringToQueryResponse(message);
        messageHandler.cosumeMessage(incomingMessage);
    }
}

