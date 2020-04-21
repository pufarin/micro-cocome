package net.gabrielkovacs.showDeliveryReports.services;

import net.gabrielkovacs.showDeliveryReports.entities.ServiceBusMessageCommand;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplate;
    private ActiveMQQueue showStockReportsQueue;
    private ActiveMQQueue apiGatewayQueue;
    private ActiveMQQueue serviceBusCommand;
    private MessageManipulation messageManipulation;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, ActiveMQQueue showStockReportsQueue, ActiveMQQueue apiGatewayQueue,
                           ActiveMQQueue serviceBusCommand, MessageManipulation messageManipulation){
        this.jmsTemplate = jmsTemplate;
        this.showStockReportsQueue = showStockReportsQueue;
        this.apiGatewayQueue = apiGatewayQueue;
        this.serviceBusCommand = serviceBusCommand;
        this.messageManipulation =  messageManipulation;
    }

    public void sendMessageToShowStockReports(String clientCallBack){
        jmsTemplate.convertAndSend(showStockReportsQueue,clientCallBack);
    }

    public void sendMessageToApiGateway(String message){
        jmsTemplate.convertAndSend(apiGatewayQueue, message);
    }

    public void getDeliveryTimePerProductId(ServiceBusMessageCommand serviceBusMessageCommand) {
        String message = messageManipulation.convertServiceBusMessageCommandToString(serviceBusMessageCommand);
        jmsTemplate.convertAndSend(serviceBusCommand, message);
    }
}
