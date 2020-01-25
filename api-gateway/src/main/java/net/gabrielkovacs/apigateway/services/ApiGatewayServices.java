package net.gabrielkovacs.apigateway.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import net.gabrielkovacs.apigateway.models.OrderDeliveryDate;
import net.gabrielkovacs.apigateway.models.ProductOrder;
import net.gabrielkovacs.apigateway.models.StockItem;
import net.gabrielkovacs.apigateway.models.SubmitedOrder;
import net.gabrielkovacs.apigateway.models.StockItemReport;

@Service
public class ApiGatewayServices {

    private final String productManagementBaseUrl = "http://localhost:8083";
    private final String createOrderPath = "/store/{id}/order";
    private final String receivedOrderPath = "/product-order/{orderEntryId}";   

    private final String stockReports = "http://localhost:8085";
    private final String getStockItemReports = "stockitemreport/{storeId}";
    private final String changePrice = "stockitem/store/{storeId}/{stockItemId}";

    private final String deliveryReports = "http://localhost:8086";

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

    public ResponseEntity<OrderDeliveryDate> receiveOrder(OrderDeliveryDate orderDeliveryDate, Long orderEntryId){
        setWebClientBaseUri(productManagementBaseUrl);
        return webClient.put()
                        .uri(receivedOrderPath, orderEntryId )
                        .bodyValue(orderDeliveryDate)
                        .exchange()
                        .flatMap(response -> response.toEntity(OrderDeliveryDate.class))
                        .block();

    }

}