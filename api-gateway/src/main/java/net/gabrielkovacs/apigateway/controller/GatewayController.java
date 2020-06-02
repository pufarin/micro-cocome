package net.gabrielkovacs.apigateway.controller;

import net.gabrielkovacs.apigateway.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;

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

    Logger log = LoggerFactory.getLogger(GatewayController.class);
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
    public ResponseEntity<?> receiveOrder(@PathVariable Long storeId, @PathVariable Long orderId, @RequestBody OrderDeliveryDate orderDeliveryDate) {

        OrderDetails orderDetails = apiGatewayServices.receiveOrder(orderDeliveryDate, orderId).getBody();
        log.info("The OrderDetails: {}", orderDetails);

        return  apiGatewayServices.getAndUpdateStockItem(orderDetails);
    }
        
    @GetMapping("enterprises/{enterpriseId}/delivery-reports")
    public ResponseEntity<List<SupplierPerformance>> getDeliveryReports(@PathVariable Long enterpriseId){

        ProductSupplierAndProducts productSupplierAndProducts = apiGatewayServices.getProductSupplierAndProducts(enterpriseId).getBody();
        ProductSupplierAndProducts deliveryTime = apiGatewayServices.getDeliveryTime(productSupplierAndProducts).getBody();
        List<SupplierPerformance> supplierPerformanceList = apiGatewayServices.generateDeliveryReport(deliveryTime).getBody();
        return ResponseEntity.ok().body(supplierPerformanceList);
    }
    
}