package net.gabrielkovacs.apigateway.services;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplate;
    private ActiveMQQueue showStockReportsQueue;
    private ActiveMQQueue orderReceiveProductsQueue;
    private ActiveMQQueue showDeliveryReportsQueue;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, ActiveMQQueue showStockReportsQueue,
                           ActiveMQQueue orderReceiveProductsQueue, ActiveMQQueue showDeliveryReportsQueue) {
        this.jmsTemplate = jmsTemplate;
        this.showStockReportsQueue = showStockReportsQueue;
        this.orderReceiveProductsQueue = orderReceiveProductsQueue;
        this.showDeliveryReportsQueue = showDeliveryReportsQueue;
    }

    public void sendMessageToShowStockReports(String clientCallBack){
        jmsTemplate.convertAndSend(showStockReportsQueue,clientCallBack);
    }

    public void sendMessageToOrderProductsAndReceiveOrderedProducts(String clientCallBack){
        jmsTemplate.convertAndSend(orderReceiveProductsQueue,clientCallBack);
    }

    public void sendMessageToShowDeliveryReports(String clientCallBack){
        jmsTemplate.convertAndSend(showDeliveryReportsQueue, clientCallBack);
    }
}
