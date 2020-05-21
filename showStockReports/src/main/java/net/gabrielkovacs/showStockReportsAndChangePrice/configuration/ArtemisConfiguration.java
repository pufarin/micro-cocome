package net.gabrielkovacs.showStockReportsAndChangePrice.configuration;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class ArtemisConfiguration {

    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public ActiveMQTopic serviceBusCommand(){
        return new ActiveMQTopic("service_bus_command");
    }

    @Bean
    public ActiveMQTopic serviceBusResponse(){
        return new ActiveMQTopic("service_bus_response");
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory());
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplateTopic() {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }
}
