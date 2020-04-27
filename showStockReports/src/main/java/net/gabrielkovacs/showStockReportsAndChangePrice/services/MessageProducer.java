package net.gabrielkovacs.showStockReportsAndChangePrice.services;

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
    private ActiveMQTopic serviceBusResponse;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, JmsTemplate jmsTemplateTopic, ActiveMQQueue showStockReportsQueue, ActiveMQQueue apiGatewayQueue,
                           ActiveMQTopic serviceBusResponse){
        this.jmsTemplate = jmsTemplate;
        this.jmsTemplateTopic = jmsTemplateTopic;
        this.showStockReportsQueue = showStockReportsQueue;
        this.apiGatewayQueue = apiGatewayQueue;
        this.serviceBusResponse = serviceBusResponse;
    }

    public void sendMessageToShowStockReports(String clientCallBack){
        jmsTemplate.convertAndSend(showStockReportsQueue,clientCallBack);
    }

    public void sendMessageToApiGateway(String message){
        jmsTemplate.convertAndSend(apiGatewayQueue, message);
    }

    public void sendMessageToServiceBusResponse(String message) {jmsTemplateTopic.convertAndSend(serviceBusResponse, message);}
}
