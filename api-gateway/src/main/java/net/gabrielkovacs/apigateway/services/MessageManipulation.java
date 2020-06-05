package net.gabrielkovacs.apigateway.services;


import com.google.gson.Gson;
import net.gabrielkovacs.apigateway.entities.OrderDetails;
import net.gabrielkovacs.apigateway.entities.ProductSupplierAndProducts;
import net.gabrielkovacs.apigateway.models.QueryResponse;
import net.gabrielkovacs.apigateway.models.StockItem;
import org.springframework.stereotype.Service;

@Service
public class MessageManipulation {

    Gson g = new Gson();

    public String convertProductSupplierAndProductsToString(ProductSupplierAndProducts productSupplierAndProducts){
        return g.toJson(productSupplierAndProducts);
    }

    public String convertQueryResponseToString(QueryResponse queryResponse){
        return g.toJson(queryResponse);
    }

    public String convertStockItemToString(StockItem stockItem){
        return g.toJson(stockItem);
    }

    public QueryResponse convertStringToQueryResponse(String message){
        return g.fromJson(message, QueryResponse.class);
    }

    public OrderDetails convertStringToOrderDetails(String message){
        return g.fromJson(message, OrderDetails.class);
    }

    public StockItem convertStringToStockItem (String message){
        return g.fromJson(message, StockItem .class);
    }

    public ProductSupplierAndProducts convertStringToProductSupplierAndProducts(String message){
        return g.fromJson(message, ProductSupplierAndProducts.class);
    }
}
