package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;


import com.google.gson.Gson;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageManipulation {

    Gson g = new Gson();


    public ClientCallBack convertStringToJSONObject(String message){
        return g.fromJson(message, ClientCallBack.class);
    }

    public ServiceBusMessageCommand getServiceBusMessageCommandFromJSON(String message){
        return  g.fromJson(message, ServiceBusMessageCommand.class);
    }

    public ServiceBusMessageResponse getServiceBusMessageResponseFromJSON(String message){
        return  g.fromJson(message, ServiceBusMessageResponse.class);
    }

    public StockItem convertStringToStockItem(String message){
        return  g.fromJson(message, StockItem.class);
    }

    public String convertQueryResponseToString(QueryResponse queryResponse){
        return g.toJson(queryResponse);
    }

    public String convertServiceBusMessageCommandToString(ServiceBusMessageCommand serviceBusMessageCommand){
        return g.toJson(serviceBusMessageCommand);
    }

    public String convertServiceBusMessageResponseToString(ServiceBusMessageResponse serviceBusMessageResponse){
        return g.toJson(serviceBusMessageResponse);
    }

    public String convertStockItemToString(StockItem stockItem) { return  g.toJson(stockItem);}

    public String convertOrderEntryToString(OrderEntry orderEntry) {return g.toJson(orderEntry);}

    public String convertProductDeliveryDurations(List<ProductDeliveryDuration> productDeliveryDurations) { return  g.toJson(productDeliveryDurations);}

    public String convertResponseToString(ResponseEntity<?> responseEntity) {return  g.toJson(responseEntity);}

    public IncomingProductOrder getIncomingProductOrderFromJSON(String data){
        return g.fromJson(data, IncomingProductOrder.class);
    }

    public ReceivedOrder getReceivedOrderFromJson(String data){
        return g.fromJson(data, ReceivedOrder.class);
    }
}
