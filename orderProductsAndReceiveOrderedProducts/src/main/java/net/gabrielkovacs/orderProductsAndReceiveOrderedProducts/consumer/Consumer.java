package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.consumer;


import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ClientCallBack;
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

    @JmsListener(destination = "order_receive_products")
    public void cosume(String message){
        log.info("Received Message in orderProductsAnsReceiveOrder: {}", message);
        ClientCallBack clientCallBack = messageManipulation.convertStringToJSONObject(message);
        messageHandler.cosumeMessage(clientCallBack);
    }
}

