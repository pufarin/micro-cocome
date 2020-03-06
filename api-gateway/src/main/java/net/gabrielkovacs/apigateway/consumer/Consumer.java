package net.gabrielkovacs.apigateway.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    Logger log = LoggerFactory.getLogger(Consumer.class);


    @JmsListener(destination = "api_gateway")
    public void cosume(String message){
        log.info("Received Message in API GATEWAY: {}", message);

    }
}

