package net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.services;


import com.google.gson.Gson;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.ClientCallBack;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.IncomingProductOrder;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.OrderEntry;
import net.gabrielkovacs.orderProductsAndReceiveOrderedProducts.entities.QueryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageManipulation {

    Gson g = new Gson();


    public ClientCallBack convertStringToJSONObject(String message){
        return g.fromJson(message, ClientCallBack.class);
    }



    public String convertQueryResponseToString(QueryResponse queryResponse){
        return g.toJson(queryResponse);
    }

    public String convertOrderEntryToString(OrderEntry orderEntry) {return g.toJson(orderEntry);}

    public IncomingProductOrder getIncomingProductOrderFromJSON(String data){
        return g.fromJson(data, IncomingProductOrder.class);
    }
}
