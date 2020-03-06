package net.gabrielkovacs.apigateway.services;

import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplate;
    private ActiveMQQueue showStockReportsQueue;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, ActiveMQQueue showStockReportsQueue){
        this.jmsTemplate = jmsTemplate;
        this.showStockReportsQueue = showStockReportsQueue;
    }

    public void sendMessageToShowStockReports(String clientCallBack){
        jmsTemplate.convertAndSend(showStockReportsQueue,clientCallBack);
    }
}
