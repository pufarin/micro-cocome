package net.gabrielkovacs.apigateway.consumer;

import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import net.gabrielkovacs.apigateway.models.QueryResponse;
import net.gabrielkovacs.apigateway.services.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    Logger log = LoggerFactory.getLogger(Consumer.class);
    private MessageHandler messageHandler;

    public Consumer(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

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
}

