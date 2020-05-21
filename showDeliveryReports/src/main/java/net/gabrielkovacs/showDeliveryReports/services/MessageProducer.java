package net.gabrielkovacs.showDeliveryReports.services;

import net.gabrielkovacs.showDeliveryReports.entities.ServiceBusMessageCommand;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplate;
    private JmsTemplate jmsTemplateTopic;
    private ActiveMQTopic serviceBusCommand;
    private MessageManipulation messageManipulation;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, JmsTemplate jmsTemplateTopic,
                           ActiveMQTopic serviceBusCommand, MessageManipulation messageManipulation){
        this.jmsTemplate = jmsTemplate;
        this.jmsTemplateTopic = jmsTemplateTopic;
        this.serviceBusCommand = serviceBusCommand;
        this.messageManipulation =  messageManipulation;
    }

    public void getDeliveryTimePerProductId(ServiceBusMessageCommand serviceBusMessageCommand) {
        String message = messageManipulation.convertServiceBusMessageCommandToString(serviceBusMessageCommand);
        jmsTemplateTopic.convertAndSend(serviceBusCommand, message);
    }
}
