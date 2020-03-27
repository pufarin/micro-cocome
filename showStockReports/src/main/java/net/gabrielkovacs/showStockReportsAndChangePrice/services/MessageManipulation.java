package net.gabrielkovacs.showStockReportsAndChangePrice.services;


import com.google.gson.Gson;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageManipulation {

    Gson g = new Gson();


    public ClientCallBack convertStringToJSONObject(String message){
        return g.fromJson(message, ClientCallBack.class);
    }

    public StockItem convertStringToStockItemObject(String message){
        return g.fromJson(message, StockItem.class);
    }

    public String convertListOfReportEntryToString(List<ReportEntry> result){

         return g.toJson(result);
    }

    public String convertQueryResponseToString(QueryResponse queryResponse){
        return g.toJson(queryResponse);
    }

    public String convertResponseEntityToString(ResponseEntity responseEntity) {return g.toJson(responseEntity);}
}
