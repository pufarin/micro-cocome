package net.gabrielkovacs.showDeliveryReports.services;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplate;
    private ActiveMQQueue showStockReportsQueue;
    private ActiveMQQueue apiGatewayQueue;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, ActiveMQQueue showStockReportsQueue, ActiveMQQueue apiGatewayQueue){
        this.jmsTemplate = jmsTemplate;
        this.showStockReportsQueue = showStockReportsQueue;
        this.apiGatewayQueue = apiGatewayQueue;
    }

    public void sendMessageToShowStockReports(String clientCallBack){
        jmsTemplate.convertAndSend(showStockReportsQueue,clientCallBack);
    }

    public void sendMessageToApiGateway(String message){
        jmsTemplate.convertAndSend(apiGatewayQueue, message);
    }

}
