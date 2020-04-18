package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderProcessingState;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ServiceBusMessageCommand;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderProcessingStateRepository;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.consumer.OrderState.request_item_stock;
import static net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.consumer.OrderState.updated_stock;

@Service
public class MessageProducer {

    //@Autowired
    private JmsTemplate jmsTemplate;
    private ActiveMQQueue showStockReportsQueue;
    private ActiveMQQueue apiGatewayQueue;
    private ActiveMQQueue serviceBusCommandQueue;
    private ActiveMQQueue serviceBusResponseQueue;
    private MessageManipulation messageManipulation;
    private OrderProcessingStateService orderProcessingStateService;
    private OrderProcessingStateRepository orderProcessingStateRepository;

    //@Autowired
    public MessageProducer(JmsTemplate jmsTemplate, ActiveMQQueue showStockReportsQueue, ActiveMQQueue apiGatewayQueue,
                           ActiveMQQueue serviceBusCommandQueue, ActiveMQQueue serviceBusResponseQueue,
                           MessageManipulation messageManipulation,
                           OrderProcessingStateService orderProcessingStateService,
                           OrderProcessingStateRepository orderProcessingStateRepository){
        this.jmsTemplate = jmsTemplate;
        this.showStockReportsQueue = showStockReportsQueue;
        this.apiGatewayQueue = apiGatewayQueue;
        this.serviceBusResponseQueue =  serviceBusResponseQueue;
        this.serviceBusCommandQueue = serviceBusCommandQueue;
        this.messageManipulation = messageManipulation;
        this.orderProcessingStateService = orderProcessingStateService;
        this.orderProcessingStateRepository = orderProcessingStateRepository;
    }

    public void sendMessageToApiGateway(String message){
        jmsTemplate.convertAndSend(apiGatewayQueue, message);
    }

    public void sendCommandToServiceBus(String message) {jmsTemplate.convertAndSend(serviceBusCommandQueue, message);}

    public void sendRequestStockItem(ServiceBusMessageCommand serviceBusMessageCommand) {
        String message = messageManipulation.convertServiceBusMessageCommandToString(serviceBusMessageCommand);
        jmsTemplate.convertAndSend(serviceBusCommandQueue, message);

        // Changing the state of the order
        OrderProcessingState orderProcessingState = orderProcessingStateRepository.findById(serviceBusMessageCommand.getCorrelationId()).get();
        String currentState = orderProcessingState.getOrderState();
        String newState = orderProcessingStateService.changeState(request_item_stock.name(),currentState);
        orderProcessingState.setOrderState(newState);
        orderProcessingStateRepository.save(orderProcessingState);
    }

    public void sendUpdateStockItem(ServiceBusMessageCommand serviceBusMessageCommand) {
        String message = messageManipulation.convertServiceBusMessageCommandToString(serviceBusMessageCommand);
        jmsTemplate.convertAndSend(serviceBusCommandQueue, message);

        // Changing the state of the order
        OrderProcessingState orderProcessingState = orderProcessingStateRepository.findById(serviceBusMessageCommand.getCorrelationId()).get();
        String currentState = orderProcessingState.getOrderState();
        String newState = orderProcessingStateService.changeState(updated_stock.name(),currentState);
        orderProcessingState.setOrderState(newState);
        orderProcessingStateRepository.save(orderProcessingState);
    }

    public void sendMessageToServiceBusResponse(String message) {jmsTemplate.convertAndSend(serviceBusResponseQueue, message);}
}
