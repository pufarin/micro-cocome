package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderProcessingState;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ServiceBusMessageCommand;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderProcessingStateRepository;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import static net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.consumer.OrderState.request_item_stock;
import static net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.consumer.OrderState.updated_stock;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplate;
    private JmsTemplate jmsTemplateTopic;
    private ActiveMQQueue showStockReportsQueue;
    private ActiveMQQueue apiGatewayQueue;
    private ActiveMQTopic serviceBusCommandTopic;
    private ActiveMQTopic serviceBusResponseTopic;
    private MessageManipulation messageManipulation;
    private OrderProcessingStateService orderProcessingStateService;
    private OrderProcessingStateRepository orderProcessingStateRepository;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, JmsTemplate jmsTemplateTopic, ActiveMQQueue showStockReportsQueue,
                           ActiveMQQueue apiGatewayQueue,
                           ActiveMQTopic serviceBusCommandTopic, ActiveMQTopic serviceBusResponseTopic,
                           MessageManipulation messageManipulation,
                           OrderProcessingStateService orderProcessingStateService,
                           OrderProcessingStateRepository orderProcessingStateRepository){
        this.jmsTemplate = jmsTemplate;
        this.jmsTemplateTopic = jmsTemplateTopic;
        this.showStockReportsQueue = showStockReportsQueue;
        this.apiGatewayQueue = apiGatewayQueue;
        this.serviceBusResponseTopic =  serviceBusResponseTopic;
        this.serviceBusCommandTopic = serviceBusCommandTopic;
        this.messageManipulation = messageManipulation;
        this.orderProcessingStateService = orderProcessingStateService;
        this.orderProcessingStateRepository = orderProcessingStateRepository;
    }

    public void sendMessageToApiGateway(String message){
        jmsTemplate.convertAndSend(apiGatewayQueue, message);
    }

    public void sendCommandToServiceBus(String message) {jmsTemplate.convertAndSend(serviceBusCommandTopic, message);}

    public void sendRequestStockItem(ServiceBusMessageCommand serviceBusMessageCommand) {
        String message = messageManipulation.convertServiceBusMessageCommandToString(serviceBusMessageCommand);
        jmsTemplateTopic.convertAndSend(serviceBusCommandTopic, message);

        // Changing the state of the order
        OrderProcessingState orderProcessingState = orderProcessingStateRepository.findById(serviceBusMessageCommand.getCorrelationId()).get();
        String currentState = orderProcessingState.getOrderState();
        String newState = orderProcessingStateService.changeState(request_item_stock.name(),currentState);
        orderProcessingState.setOrderState(newState);
        orderProcessingStateRepository.save(orderProcessingState);
    }

    public void sendUpdateStockItem(ServiceBusMessageCommand serviceBusMessageCommand) {
        String message = messageManipulation.convertServiceBusMessageCommandToString(serviceBusMessageCommand);
        jmsTemplateTopic.convertAndSend(serviceBusCommandTopic, message);

        // Changing the state of the order
        OrderProcessingState orderProcessingState = orderProcessingStateRepository.findById(serviceBusMessageCommand.getCorrelationId()).get();
        String currentState = orderProcessingState.getOrderState();
        String newState = orderProcessingStateService.changeState(updated_stock.name(),currentState);
        orderProcessingState.setOrderState(newState);
        orderProcessingStateRepository.save(orderProcessingState);
    }

    public void sendMessageToServiceBusResponse(String message) {jmsTemplateTopic.convertAndSend(serviceBusResponseTopic, message);}
}
