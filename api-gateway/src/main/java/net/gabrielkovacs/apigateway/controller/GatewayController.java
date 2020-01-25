package net.gabrielkovacs.apigateway.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;

import net.gabrielkovacs.apigateway.models.OrderDeliveryDate;
import net.gabrielkovacs.apigateway.models.ProductOrder;
import net.gabrielkovacs.apigateway.models.StockItem;
import net.gabrielkovacs.apigateway.models.StockItemReport;
import net.gabrielkovacs.apigateway.models.SubmitedOrder;
import net.gabrielkovacs.apigateway.models.SupplierPerformance;
import net.gabrielkovacs.apigateway.services.ApiGatewayServices;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
public class GatewayController {

    private ApiGatewayServices apiGatewayServices;

    public GatewayController (ApiGatewayServices apiGatewayServices){
        this.apiGatewayServices = apiGatewayServices;

    }

    @PostMapping("stores/{storeId}/orders")
    public ResponseEntity<ProductOrder> createOrder(@RequestBody SubmitedOrder submitedOrder, @PathVariable Long storeId) {
                
        return apiGatewayServices.submitProductOrder(submitedOrder, storeId);
    }
    
    @GetMapping("stores/{storeId}/stock-item-reports")
    public ResponseEntity<List<StockItemReport>> getStockItemReports(@PathVariable Long storeId){
        return apiGatewayServices.getStockItemReports(storeId);
    }

    @PutMapping("stores/{storeId}/stockitems/{stockItemId}")
    public ResponseEntity<StockItem> chengeSrockItemPrice(@PathVariable Long storeId,@PathVariable Long stockItemId, @RequestBody StockItem stockItem) {
               
        return apiGatewayServices.changeProductPrice(storeId, stockItemId, stockItem);

    }    
    @PutMapping("stores/{storeId}/orders/{orderId}")
    public ResponseEntity<OrderDeliveryDate> receiveOrder(@PathVariable Long storeId, @PathVariable Long orderId, @RequestBody OrderDeliveryDate orderDeliveryDate) {
        return apiGatewayServices.receiveOrder(orderDeliveryDate, orderId);
        
    }
        
    @GetMapping("enterprises/{enterpriseId}/delivery-reports")
    public ResponseEntity<List<SupplierPerformance>> getDeliveryReports(@PathVariable Long enterpriseId){
        return apiGatewayServices.getDeliveryReports(enterpriseId);
    }
    
}