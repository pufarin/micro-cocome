package net.gabrielkovacs.showDeliveryReports.configuration;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class ArtemisConfiguration {

    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public ActiveMQQueue apiGatewayQueue(){
        return new ActiveMQQueue("api_gateway");
    }

    @Bean
    public ActiveMQQueue showStockReportsQueue(){
        return new ActiveMQQueue("show_stock_reports");
    }

    @Bean
    public ActiveMQQueue showDeliveryReportsQueue(){
        return new ActiveMQQueue("show_delivery_reports");
    }

    @Bean
    public ActiveMQQueue orderReceiveProductsQueue(){
        return new ActiveMQQueue("order_receive_products");
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(activeMQConnectionFactory());
    }

}
