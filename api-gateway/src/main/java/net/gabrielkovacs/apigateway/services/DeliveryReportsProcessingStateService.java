package net.gabrielkovacs.apigateway.services;

import net.gabrielkovacs.apigateway.repository.DeliveryReportsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReportsProcessingStateService {

    Logger log = LoggerFactory.getLogger(DeliveryReportsProcessingStateService.class);


    static final String stateA = "initiated";
    static final String stateB = "generateProductSupplierAndProducts";
    static final String stateC = "getDeliveryDuration";
    static final String stateD = "generateDeliveryReport";
    static final String stateE = "finished";

    private DeliveryReportsRepository deliveryReportsRepository;

    public DeliveryReportsProcessingStateService(DeliveryReportsRepository deliveryReportsRepository){
        this.deliveryReportsRepository = deliveryReportsRepository;

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
