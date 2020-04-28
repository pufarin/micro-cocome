package net.gabrielkovacs.showDeliveryReports.services;

import net.gabrielkovacs.showDeliveryReports.entities.ServiceBusMessageCommand;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplate;
    private JmsTemplate jmsTemplateTopic;
    private ActiveMQQueue showStockReportsQueue;
    private ActiveMQQueue apiGatewayQueue;
    private ActiveMQTopic serviceBusCommand;
    private MessageManipulation messageManipulation;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, JmsTemplate jmsTemplateTopic, ActiveMQQueue showStockReportsQueue, ActiveMQQueue apiGatewayQueue,
                           ActiveMQTopic serviceBusCommand, MessageManipulation messageManipulation){
        this.jmsTemplate = jmsTemplate;
        this.jmsTemplateTopic = jmsTemplateTopic;
        this.showStockReportsQueue = showStockReportsQueue;
        this.apiGatewayQueue = apiGatewayQueue;
        this.serviceBusCommand = serviceBusCommand;
        this.messageManipulation =  messageManipulation;
    }

    public void sendMessageToApiGateway(String message){
        jmsTemplate.convertAndSend(apiGatewayQueue, message);
    }

    public void getDeliveryTimePerProductId(ServiceBusMessageCommand serviceBusMessageCommand) {
        String message = messageManipulation.convertServiceBusMessageCommandToString(serviceBusMessageCommand);
        jmsTemplateTopic.convertAndSend(serviceBusCommand, message);
    }
}
