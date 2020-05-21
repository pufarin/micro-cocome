package net.gabrielkovacs.showDeliveryReports.consumer;


import net.gabrielkovacs.showDeliveryReports.entities.ServiceBusMessageResponse;
import net.gabrielkovacs.showDeliveryReports.services.MessageHandler;
import net.gabrielkovacs.showDeliveryReports.services.MessageManipulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    Logger log = LoggerFactory.getLogger(Consumer.class);
    private MessageManipulation messageManipulation;
    private MessageHandler messageHandler;

    public Consumer(MessageManipulation messageManipulation, MessageHandler messageHandler) {
        this.messageManipulation = messageManipulation;
        this.messageHandler = messageHandler;

    }

    @JmsListener(destination = "service_bus_response", containerFactory = "jmsListenerContainerFactory")
    public void consumeServiceBusResponse(String message){
        log.info("Received Message in orderProductsAnsReceiveOrder ServiceBusResponse : {}", message);
        ServiceBusMessageResponse serviceBusMessageResponse = messageManipulation.getServiceBusMessageResponseFromJSON(message);
        messageHandler.consumeServiceBusMessageResponse(serviceBusMessageResponse);
    }
}

