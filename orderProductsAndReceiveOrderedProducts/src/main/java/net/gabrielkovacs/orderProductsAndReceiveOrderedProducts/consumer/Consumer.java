package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.consumer;


import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ClientCallBack;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ServiceBusMessageCommand;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ServiceBusMessageResponse;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services.MessageHandler;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services.MessageManipulation;
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

    @JmsListener(destination = "order_receive_products", containerFactory = "jmsListenerContainerFactoryQueue")
    public void consume(String message){
        log.info("Received Message in orderProductsAnsReceiveOrder: {}", message);
        ClientCallBack clientCallBack = messageManipulation.convertStringToJSONObject(message);
        messageHandler.cosumeMessage(clientCallBack);

    }

    @JmsListener(destination = "service_bus_command", containerFactory = "jmsListenerContainerFactory")
    public void consumeServiceBusCommand(String message){
        log.info("Received Message in orderProductsAnsReceiveOrder ServiceBusCommand : {}", message);
        ServiceBusMessageCommand serviceBusMessageCommand = messageManipulation.getServiceBusMessageCommandFromJSON(message);
        messageHandler.consumeServiceBusMessageCommand(serviceBusMessageCommand);
    }

    @JmsListener(destination = "service_bus_response", containerFactory = "jmsListenerContainerFactory")
    public void consumeServiceBusResponse(String message){
        log.info("Received Message in orderProductsAnsReceiveOrder ServiceBusResponse : {}", message);
        ServiceBusMessageResponse serviceBusMessageResponse = messageManipulation.getServiceBusMessageResponseFromJSON(message);
        messageHandler.consumeServiceBusMessageResponse(serviceBusMessageResponse);
    }

}

