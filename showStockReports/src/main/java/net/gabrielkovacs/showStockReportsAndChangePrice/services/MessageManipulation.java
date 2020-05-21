package net.gabrielkovacs.showStockReportsAndChangePrice.services;


import com.google.gson.Gson;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ServiceBusMessageCommand;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ServiceBusMessageResponse;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.StockItem;
import org.springframework.stereotype.Service;

@Service
public class MessageManipulation {

    Gson g = new Gson();


    public StockItem convertStringToStockItemObject(String message){
        return g.fromJson(message, StockItem.class);
    }

    public ServiceBusMessageCommand getServiceBusMessageCommandFromJSON(String message){
        return  g.fromJson(message, ServiceBusMessageCommand.class);
    }

    public String convertStockItemToString(StockItem stockItem) { return  g.toJson(stockItem);}

    public String convertServiceBusMessageResponseToString(ServiceBusMessageResponse serviceBusMessageResponse ){
        return g.toJson(serviceBusMessageResponse);
    }
}
