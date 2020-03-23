package net.gabrielkovacs.apigateway.controller;

import com.google.gson.Gson;
import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import net.gabrielkovacs.apigateway.models.*;
import net.gabrielkovacs.apigateway.repository.ClientCallBackRepository;
import net.gabrielkovacs.apigateway.services.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import net.gabrielkovacs.apigateway.services.ApiGatewayServices;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class GatewayController {

    private ApiGatewayServices apiGatewayServices;
    private ClientCallBackRepository clientCallBackRepository;
    private MessageProducer messageProducer;
    private Gson gson = new Gson();
    Logger log = LoggerFactory.getLogger(GatewayController.class);


    public GatewayController(ApiGatewayServices apiGatewayServices, ClientCallBackRepository clientCallBackRepository, MessageProducer messageProducer){
        this.apiGatewayServices = apiGatewayServices;
        this.clientCallBackRepository = clientCallBackRepository;
        this.messageProducer = messageProducer;
    }
/*
    @PostMapping("stores/{storeId}/orders")
    public ResponseEntity<ProductOrder> createOrder(@RequestBody SubmitedOrder submitedOrder, @PathVariable Long storeId) {
                
        return apiGatewayServices.submitProductOrder(submitedOrder, storeId);
    }
*/
    @PostMapping("stores/{storeId}/orders")
    public String createOrder(@RequestBody SubmitedOrder submitedOrder, @PathVariable Long storeId,
                                                    @RequestParam String call_back) {
        Date date= new Date();
        SubmitedOrderWithStoreId submitedOrderWithStoreId = new SubmitedOrderWithStoreId(submitedOrder.getAmount(),
                                                            submitedOrder.getAmount(), storeId);
        ClientCallBack clientCallBack = new ClientCallBack(apiGatewayServices.generateCorrelationId(), call_back,
                new Timestamp( date.getTime()),"orderProduct", gson.toJson(submitedOrderWithStoreId));

        clientCallBackRepository.save(clientCallBack);

        messageProducer.sendMessageToOrderProductsAndReceiveOrderedProducts(gson.toJson(clientCallBack));
        log.info("Get StockItemReport Message: {}", gson.toJson(clientCallBack));
        return "Store ID: " + storeId + "Call Back: " + call_back + "message: " + gson.toJson(clientCallBack);


    }
 /*
    @GetMapping("stores/{storeId}/stock-item-reports")
    public ResponseEntity<List<StockItemReport>> getStockItemReports(@PathVariable Long storeId){
        return apiGatewayServices.getStockItemReports(storeId);
    }
*/
    @GetMapping("stores/{storeId}/stock-item-reports")
    public String getStockItemReports(@PathVariable Long storeId, @RequestParam String call_back){
        Date date= new Date();
        ClientCallBack clientCallBack = new ClientCallBack(apiGatewayServices.generateCorrelationId(), call_back,
                                        new Timestamp( date.getTime()),"returnStockItemReports", storeId.toString());
        clientCallBackRepository.save(clientCallBack);
//        messageProducer.sendMessageToShowStockReports(apiGatewayServices.generateJSONString(clientCallBack));
//        log.info("Get StockItemReport Message: {}", apiGatewayServices.generateJSONString(clientCallBack));

        messageProducer.sendMessageToShowStockReports(apiGatewayServices.generateJSONStringFromClass(clientCallBack));
        log.info("Get StockItemReport Message: {}", apiGatewayServices.generateJSONStringFromClass(clientCallBack));
        return "Store ID: " + storeId + "Call Back: " + call_back + "message: " + apiGatewayServices.generateJSONString(clientCallBack);
    }

 /*   @PutMapping("stores/{storeId}/stockitems/{stockItemId}")
    public ResponseEntity<StockItem> chengeSrockItemPrice(@PathVariable Long storeId,@PathVariable Long stockItemId, @RequestBody StockItem stockItem) {
               
        return apiGatewayServices.changeProductPrice(storeId, stockItemId, stockItem);

    }*/

    @PutMapping("stores/{storeId}/stockitems/{stockItemId}")
    public String chengeSrockItemPrice(@PathVariable Long storeId,@PathVariable Long stockItemId, @RequestBody StockItem stockItem, @RequestParam String call_back) {
        Date date= new Date();

        ClientCallBack clientCallBack = new ClientCallBack(apiGatewayServices.generateCorrelationId(),call_back,
                                        new Timestamp( date.getTime()),"changeStockItemPrice",
                                        gson.toJson(stockItem));

        clientCallBackRepository.save(clientCallBack);

        messageProducer.sendMessageToShowStockReports(apiGatewayServices.generateJSONStringFromClass(clientCallBack));
        log.info("Get StockItemReport Message: {}", apiGatewayServices.generateJSONStringFromClass(clientCallBack));
        return "Store ID: " + storeId + "Call Back: " + call_back + "message: " + gson.toJson(clientCallBack);

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