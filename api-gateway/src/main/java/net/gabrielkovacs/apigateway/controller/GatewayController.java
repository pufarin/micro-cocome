package net.gabrielkovacs.apigateway.controller;

import com.google.gson.Gson;
import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import net.gabrielkovacs.apigateway.entities.DeliveryReportsProcessingState;
import net.gabrielkovacs.apigateway.entities.OrderProcessingState;
import net.gabrielkovacs.apigateway.models.*;
import net.gabrielkovacs.apigateway.repository.ClientCallBackRepository;
import net.gabrielkovacs.apigateway.repository.DeliveryReportsRepository;
import net.gabrielkovacs.apigateway.repository.OrderProcessingStateRepository;
import net.gabrielkovacs.apigateway.services.DeliveryReportsProcessingStateService;
import net.gabrielkovacs.apigateway.services.MessageProducer;
import net.gabrielkovacs.apigateway.services.OrderProcessingStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import net.gabrielkovacs.apigateway.services.ApiGatewayServices;

import java.sql.Timestamp;
import java.util.Date;

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
    private DeliveryReportsRepository deliveryReportsRepository;
    private DeliveryReportsProcessingStateService deliveryReportsProcessingStateService;
    private OrderProcessingStateRepository orderProcessingStateRepository;
    private OrderProcessingStateService orderProcessingStateService;
    private Gson gson = new Gson();
    Logger log = LoggerFactory.getLogger(GatewayController.class);


    public GatewayController(ApiGatewayServices apiGatewayServices, ClientCallBackRepository clientCallBackRepository,
                             MessageProducer messageProducer, DeliveryReportsRepository deliveryReportsRepository,
                             DeliveryReportsProcessingStateService deliveryReportsProcessingStateService, OrderProcessingStateRepository orderProcessingStateRepository, OrderProcessingStateService orderProcessingStateService){
        this.apiGatewayServices = apiGatewayServices;
        this.clientCallBackRepository = clientCallBackRepository;
        this.messageProducer = messageProducer;
        this.deliveryReportsRepository = deliveryReportsRepository;
        this.deliveryReportsProcessingStateService = deliveryReportsProcessingStateService;
        this.orderProcessingStateRepository = orderProcessingStateRepository;
        this.orderProcessingStateService = orderProcessingStateService;
    }

    @PostMapping("stores/{storeId}/orders")
    public ResponseEntity createOrder(@RequestBody SubmitedOrder submitedOrder, @PathVariable Long storeId,
                                                    @RequestParam String call_back) {
        Date date= new Date();
        SubmitedOrderWithStoreId submitedOrderWithStoreId = new SubmitedOrderWithStoreId(submitedOrder.getAmount(),
                                                            submitedOrder.getProductId(), storeId);
        ClientCallBack clientCallBack = new ClientCallBack(apiGatewayServices.generateCorrelationId(), call_back,
                new Timestamp( date.getTime()),"orderProduct", gson.toJson(submitedOrderWithStoreId));

        clientCallBackRepository.save(clientCallBack);

        messageProducer.sendMessageToOrderProductsAndReceiveOrderedProducts(gson.toJson(clientCallBack));
        log.info("Create new product order: {}", gson.toJson(clientCallBack));

        return ResponseEntity.accepted().body(clientCallBack);

    }

    @GetMapping("stores/{storeId}/stock-item-reports")
    public ResponseEntity getStockItemReports(@PathVariable Long storeId, @RequestParam String call_back){
        Date date= new Date();
        ClientCallBack clientCallBack = new ClientCallBack(apiGatewayServices.generateCorrelationId(), call_back,
                                        new Timestamp( date.getTime()),"returnStockItemReports", storeId.toString());
        clientCallBackRepository.save(clientCallBack);

        messageProducer.sendMessageToShowStockReports(apiGatewayServices.generateJSONStringFromClass(clientCallBack));
        log.info("Get StockItemReport Message: {}", apiGatewayServices.generateJSONStringFromClass(clientCallBack));

        return ResponseEntity.accepted().body(clientCallBack);
    }

    @PutMapping("stores/{storeId}/stockitems/{stockItemId}")
    public ResponseEntity chengeSrockItemPrice(@PathVariable Long storeId, @PathVariable Long stockItemId, @RequestBody StockItem stockItem, @RequestParam String call_back) {
        Date date= new Date();

        ClientCallBack clientCallBack = new ClientCallBack(apiGatewayServices.generateCorrelationId(),call_back,
                                        new Timestamp( date.getTime()),"changeStockItemPrice",
                                        gson.toJson(stockItem));

        clientCallBackRepository.save(clientCallBack);

        messageProducer.sendMessageToShowStockReports(apiGatewayServices.generateJSONStringFromClass(clientCallBack));
        log.info("Change stock item price Message: {}", apiGatewayServices.generateJSONStringFromClass(clientCallBack));

        return ResponseEntity.accepted().body(clientCallBack);

    }

    @PutMapping("stores/{storeId}/orders/{orderId}")
    public ResponseEntity receiveOrder(@PathVariable Long storeId, @PathVariable Long orderId, @RequestBody OrderDeliveryDate orderDeliveryDate, @RequestParam String call_back) {
        Date date= new Date();
        String correlationId = apiGatewayServices.generateCorrelationId();

        ClientCallBack clientCallBack = new ClientCallBack(correlationId, call_back,
                new Timestamp( date.getTime()),"receiveOrder",
                gson.toJson(new ReceivedOrder(orderDeliveryDate.getDeliveryDate(), storeId, orderId )));

        clientCallBackRepository.save(clientCallBack);
        clientCallBack.setEventName("updateProductOrderDeliveryDate");

        OrderProcessingState ops = new OrderProcessingState(correlationId,"initiated","receiveOrder",orderId, 0L);
        orderProcessingStateRepository.save(ops);

        messageProducer.sendMessageToOrderProductsAndReceiveOrderedProducts(gson.toJson(clientCallBack));

        String newState = orderProcessingStateService.changeState("updateProductOrderDeliveryDate",
                ops.getOrderState());
        ops.setOrderState(newState);
        orderProcessingStateRepository.save(ops);
        log.info("Received order for product: {}", gson.toJson(clientCallBack));
        return ResponseEntity.accepted().body(clientCallBack);

    }

    @GetMapping("enterprises/{enterpriseId}/delivery-reports")
    public ResponseEntity getDeliveryReports(@PathVariable Long enterpriseId, @RequestParam String call_back){
        Date date= new Date();
        String correlationId = apiGatewayServices.generateCorrelationId();
        ClientCallBack clientCallBack = new ClientCallBack( correlationId, call_back,
                new Timestamp( date.getTime()),"returnDeliveryReports", enterpriseId.toString());

        clientCallBackRepository.save(clientCallBack);
        clientCallBack.setEventName("generateProductSupplierAndProducts");

        DeliveryReportsProcessingState deliveryRPS = new DeliveryReportsProcessingState(correlationId, "initiated",
                                                    "returnDeliveryReports",enterpriseId.toString());
        deliveryReportsRepository.save(deliveryRPS);

        messageProducer.sendMessageToShowDeliveryReports(apiGatewayServices.generateJSONStringFromClass(clientCallBack));

        String newState = deliveryReportsProcessingStateService.changeState("generateProductSupplierAndProducts",
                deliveryRPS.getDeliveryReportsState());
        deliveryRPS.setDeliveryReportsState(newState);
        deliveryReportsRepository.save(deliveryRPS);
        log.info("Get DeliveryReports Message: {}", apiGatewayServices.generateJSONStringFromClass(clientCallBack));
        return ResponseEntity.accepted().body(clientCallBack);
    }
}
