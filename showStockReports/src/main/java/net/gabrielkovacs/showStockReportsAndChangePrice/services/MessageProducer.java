package net.gabrielkovacs.showStockReportsAndChangePrice.services;

import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplateTopic;
    private ActiveMQTopic serviceBusResponse;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplateTopic, ActiveMQTopic serviceBusResponse){
         this.jmsTemplateTopic = jmsTemplateTopic;
        this.serviceBusResponse = serviceBusResponse;
    }

    public void sendMessageToServiceBusResponse(String message) {jmsTemplateTopic.convertAndSend(serviceBusResponse, message);}
}
