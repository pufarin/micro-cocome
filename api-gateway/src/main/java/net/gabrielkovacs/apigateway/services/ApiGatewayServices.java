package net.gabrielkovacs.apigateway.services;

import java.util.List;

import net.gabrielkovacs.apigateway.controller.GatewayController;
import net.gabrielkovacs.apigateway.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApiGatewayServices {

    Logger log = LoggerFactory.getLogger(ApiGatewayServices.class);

    private final String productManagementBaseUrl = "http://swa-kovacs-vm3.cs.univie.ac.at/oprop";
    private final String createOrderPath = "/store/{id}/order";
    private final String receivedOrderPath = "/product-order/{orderEntryId}";
    private final String deliveryTime = "/product-order/delivery-time";

    private final String stockReports = "http://swa-kovacs-vm2.cs.univie.ac.at/sr";
    private final String getStockItemReports = "/stockitemreport/{storeId}";
    private final String changePrice = "/stockitem/store/{storeId}/{stockItemId}";
    private final String getStockItemByStoreIdAnsProductId = "/stockitem?storeId={storeId}&productId={productId}";
    private final String updateStockItemAmount = "/stockitem/{stockItemId}";

    private final String deliveryReports = "http://swa-kovacs-vm4.cs.univie.ac.at/dr";
    private final String getDeliveryReports = "/delivery-report/generation";
    private final String getSupplierAndProducts = "/delivery-report/supplier-and-products/{enterpriseId}";

    private WebClient webClient;

    public void setWebClientBaseUri(String baseUri) {
        this.webClient = WebClient.create(baseUri);
    }

    public ResponseEntity<ProductOrder> submitProductOrder(SubmitedOrder submitedOrder, Long storeId) {
        setWebClientBaseUri(productManagementBaseUrl);
        return webClient.post().uri(createOrderPath, storeId).bodyValue(submitedOrder).exchange()
                .flatMap(response -> response.toEntity(ProductOrder.class)).block();

    }

    public ResponseEntity<List<StockItemReport>> getStockItemReports(Long storeId) {
        setWebClientBaseUri(stockReports);
        return webClient.get().uri(getStockItemReports, storeId).exchange()
                .flatMap(response -> response.toEntityList(StockItemReport.class)).block();

    }

    public ResponseEntity<StockItem> changeProductPrice(Long storeId, Long stockItemId, StockItem stockItem) {
        setWebClientBaseUri(stockReports);
        return webClient.put()
                        .uri(changePrice, storeId, stockItemId)
                        .bodyValue(stockItem)
                        .exchange()
                        .flatMap(response -> response.toEntity(StockItem.class))
                        .block();
    }


    public ResponseEntity<StockItem> getStockItem(long storeId, long productId){
        setWebClientBaseUri(stockReports);
        return webClient.get()
                .uri(getStockItemByStoreIdAnsProductId, storeId,productId)
                .exchange()
                .flatMap(response -> response.toEntity(StockItem.class))
                .block();
    }

    public ResponseEntity<?> updateStockItemAmount(StockItem stockItem, long stockItemId ){
        setWebClientBaseUri(stockReports);
        return webClient.put().uri(updateStockItemAmount, stockItemId)
                .syncBody(stockItem)
                .exchange()
                .flatMap(response -> response.toEntity(StockItem.class))
                // .retrieve()
                // .bodyToMono(ResponseEntity.class)
                .block();
    }

    public ResponseEntity<?> getAndUpdateStockItem(OrderDetails orderDetails){
        ResponseEntity<StockItem> stockItemQueryResult =  getStockItem(orderDetails.getStoreId(), orderDetails.getProductId());
        if(stockItemQueryResult.getStatusCode().equals(HttpStatus.OK)){
            StockItem stockItem = stockItemQueryResult.getBody();
            long existingAmount = stockItem.getAmount();
            stockItem.setAmount(existingAmount + orderDetails.getAmount());
            log.info("The Updated StockItem: {}", stockItem);
            ResponseEntity<?> updateStockItemAmountRequest = updateStockItemAmount(stockItem,stockItem.getId());

            if(updateStockItemAmountRequest.getStatusCode().equals(HttpStatus.OK)){
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Product amount could not be updated",HttpStatus.INTERNAL_SERVER_ERROR);
            }


        }else{
            return new ResponseEntity<>("Could not retrieve product to be updated",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<OrderDetails> receiveOrder(OrderDeliveryDate orderDeliveryDate, Long orderEntryId){
        setWebClientBaseUri(productManagementBaseUrl);
        return webClient.put()
                        .uri(receivedOrderPath, orderEntryId )
                        .bodyValue(orderDeliveryDate)
                        .exchange()
                        .flatMap(response -> response.toEntity(OrderDetails.class))
                        .block();

    }

    public ResponseEntity<ProductSupplierAndProducts> getProductSupplierAndProducts (Long enterpriseId){
        setWebClientBaseUri(deliveryReports);
        return webClient.get()
                .uri(getSupplierAndProducts,enterpriseId)
                .exchange()
                .flatMap(response -> response.toEntity(ProductSupplierAndProducts.class))
                .block();

    }

    public ResponseEntity<ProductSupplierAndProducts> getDeliveryTime(ProductSupplierAndProducts productSupplierAndProducts) {
        setWebClientBaseUri(productManagementBaseUrl);
        return webClient.post().uri(deliveryTime).bodyValue(productSupplierAndProducts).exchange()
                .flatMap(response -> response.toEntity(ProductSupplierAndProducts.class)).block();

    }

    public ResponseEntity<List<SupplierPerformance>> generateDeliveryReport(ProductSupplierAndProducts deliveryTime) {
        setWebClientBaseUri(deliveryReports);
        return webClient.post().uri(getDeliveryReports).bodyValue(deliveryTime).exchange()
                .flatMap(response -> response.toEntityList(SupplierPerformance.class)).block();

    }

}
