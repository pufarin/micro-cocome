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



    public String convertQueryResponseToString(QueryResponse queryResponse){
        return g.toJson(queryResponse);
    }

    public String convertOrderEntryToString(OrderEntry orderEntry) {return g.toJson(orderEntry);}

    public String convertResponseToString(ResponseEntity<?> responseEntity) {return  g.toJson(responseEntity);}

    public String convertProductSupplierAndProductsToString(ProductSupplierAndProducts productSupplierAndProducts){
        return g.toJson(productSupplierAndProducts);
    }

    public IncomingProductOrder getIncomingProductOrderFromJSON(String data){
        return g.fromJson(data, IncomingProductOrder.class);
    }

    public ProductSupplierAndProducts convertStringToProductSupplierAndProducts(String message){
        return g.fromJson(message, ProductSupplierAndProducts.class);
    }

    public ReceivedOrder getReceivedOrderFromJson(String data){
        return g.fromJson(data, ReceivedOrder.class);
    }
}
