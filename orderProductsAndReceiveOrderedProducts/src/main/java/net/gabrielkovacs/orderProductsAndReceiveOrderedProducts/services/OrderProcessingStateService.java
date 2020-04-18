package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;

import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderProcessingState;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.repository.OrderProcessingStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingStateService {

    Logger log = LoggerFactory.getLogger(OrderProcessingStateService.class);


    static final String stateA = "initiated";
    static final String stateB = "request_item_stock";
    static final String stateC = "computed_new_stock";
    static final String stateD = "updated_stock";
    static final String stateE = "finished_update";

    private OrderProcessingStateRepository orderProcessingStateRepository;

    public OrderProcessingStateService(OrderProcessingStateRepository orderProcessingStateRepository){
        this.orderProcessingStateRepository = orderProcessingStateRepository;

    }

    public String changeState(String input, String currentState ){
        String newState = currentState;
        switch(currentState){
            case(stateA):
                if(input.equals(stateB)){ newState = stateB;};
                break;
            case(stateB):
                if(input.equals(stateC)){ newState = stateC;};
                break;
            case(stateC):
                if(input.equals(stateD)){ newState = stateD;};
                break;
            case(stateD):
                if(input.equals(stateE)){ newState = stateE;};
            case(stateE):
                newState = stateE;
        }
        return newState;
    }
}
