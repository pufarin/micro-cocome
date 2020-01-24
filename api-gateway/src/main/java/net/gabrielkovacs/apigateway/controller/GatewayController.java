package net.gabrielkovacs.apigateway.controller;

import org.springframework.web.bind.annotation.RestController;

import net.gabrielkovacs.apigateway.models.ProductOrder;
import net.gabrielkovacs.apigateway.models.StockItemReport;
import net.gabrielkovacs.apigateway.models.SubmitedOrder;
import net.gabrielkovacs.apigateway.services.ApiGatewayServices;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class GatewayController {

    private ApiGatewayServices apiGatewayServices;

    public GatewayController (ApiGatewayServices apiGatewayServices){
        this.apiGatewayServices = apiGatewayServices;

    }

    @PostMapping("stores/{storeId}/orders")
    public ResponseEntity<ProductOrder> postMethodName(@RequestBody SubmitedOrder submitedOrder, @PathVariable Long storeId) {
        //TODO: process POST request
        
        return apiGatewayServices.submitProductOrder(submitedOrder, storeId);
    }
    
    @GetMapping("stores/{storeId}/stock-item-reports")
    public ResponseEntity<List<StockItemReport>> getStockItemReports(@PathVariable Long storeId){
        return apiGatewayServices.getStockItemReports(storeId);
    }
}