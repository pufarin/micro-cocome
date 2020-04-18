package net.gabrielkovacs.showStockReportsAndChangePrice.services;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplate;
    private ActiveMQQueue showStockReportsQueue;
    private ActiveMQQueue apiGatewayQueue;
    private ActiveMQQueue serviceBusResponse;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, ActiveMQQueue showStockReportsQueue, ActiveMQQueue apiGatewayQueue,
                           ActiveMQQueue serviceBusResponse){
        this.jmsTemplate = jmsTemplate;
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

    public void sendMessageToServiceBusResponse(String message) {jmsTemplate.convertAndSend(serviceBusResponse, message);}
}
