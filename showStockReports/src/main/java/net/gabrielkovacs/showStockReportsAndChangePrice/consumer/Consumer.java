package net.gabrielkovacs.showStockReportsAndChangePrice.consumer;

import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ClientCallBack;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ServiceBusMessageCommand;
import net.gabrielkovacs.showStockReportsAndChangePrice.services.MessageHandler;
import net.gabrielkovacs.showStockReportsAndChangePrice.services.MessageManipulation;
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

    @JmsListener(destination = "show_stock_reports")
    public void cosume(String message){
        log.info("Received Message in ShowStockReports: {}", message);
        ClientCallBack clientCallBack = messageManipulation.convertStringToJSONObject(message);
        messageHandler.cosumeMessage(clientCallBack);
    }

    @JmsListener(destination = "service_bus_command")
    public void consumeServiceBusCommand(String message){
        log.info("Received Message in orderProductsAnsReceiveOrder ServiceBusCommand : {}", message);
        ServiceBusMessageCommand serviceBusMessageCommand = messageManipulation.getServiceBusMessageCommandFromJSON(message);
        messageHandler.consumeServiceBusMessageCommand(serviceBusMessageCommand);
    }
}
