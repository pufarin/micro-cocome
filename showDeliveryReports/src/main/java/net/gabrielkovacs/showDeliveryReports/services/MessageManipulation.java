package net.gabrielkovacs.showDeliveryReports.services;


import com.google.gson.Gson;
import net.gabrielkovacs.showDeliveryReports.entities.ClientCallBack;
import net.gabrielkovacs.showDeliveryReports.entities.DeliveryReport;
import net.gabrielkovacs.showDeliveryReports.entities.ProductSupplierAndProducts;
import net.gabrielkovacs.showDeliveryReports.entities.QueryResponse;
import org.springframework.stereotype.Service;

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

    public String convertProductSupplierAndProductsToString(ProductSupplierAndProducts productSupplierAndProducts){
        return g.toJson(productSupplierAndProducts);
    }

    public String convertQueryResponseToString(QueryResponse queryResponse){
        return g.toJson(queryResponse);
    }

    public ProductSupplierAndProducts convertStringToProductSupplierAndProducts(String message){
        return g.fromJson(message, ProductSupplierAndProducts.class);
    }
}
