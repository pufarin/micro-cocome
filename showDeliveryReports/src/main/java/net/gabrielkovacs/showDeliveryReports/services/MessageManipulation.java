package net.gabrielkovacs.showDeliveryReports.services;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.gabrielkovacs.showDeliveryReports.entities.*;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageManipulation {

    Gson g = new Gson();


    public ClientCallBack convertStringToJSONObject(String message){
        return g.fromJson(message, ClientCallBack.class);
    }

    public String convertListOfDeliveryReportsToString(List<DeliveryReport> deliveryReports){
        return g.toJson(deliveryReports);
    }

    public String convertQueryResponseToString(QueryResponse queryResponse){
        return g.toJson(queryResponse);
    }

    public String convertServiceBusMessageCommandToString(ServiceBusMessageCommand serviceBusMessageCommand){
        return g.toJson(serviceBusMessageCommand);

    }

    public ServiceBusMessageResponse getServiceBusMessageResponseFromJSON(String message){
        return  g.fromJson(message, ServiceBusMessageResponse.class);
    }

    public List<ProductDeliveryDuration> convertJsonToProductDeliveryDurations(String document){
        Type listType = new TypeToken<ArrayList<ProductDeliveryDuration>>() {}.getType();
        return g.fromJson(document,listType );
    }

    public String convertSupplyChainDataToString(ProductSupplierAndProducts theData){
        return g.toJson(theData);
    }

    public ProductSupplierAndProducts jsonStringToProductSupplierAndProducts(String theData){
        return  g.fromJson(theData, ProductSupplierAndProducts.class);
    }
}