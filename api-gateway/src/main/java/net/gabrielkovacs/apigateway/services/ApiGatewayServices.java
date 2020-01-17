package net.gabrielkovacs.apigateway.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import net.gabrielkovacs.apigateway.models.ProductOrder;
import net.gabrielkovacs.apigateway.models.SubmitedOrder;

@Service
public class ApiGatewayServices{


    private final String productManagementBaseUrl = "http://localhost:8083";
    private final String createOrderPath = "/store/{id}/order";

    private final String stockReports = "http://localhost:8085";
    private final String deliveryReports = "http://localhost:8086";

    private WebClient webClient;
    
    public void setWebClientBaseUri(String baseUri){
        this.webClient = WebClient.create(baseUri);
    }

    public ResponseEntity<ProductOrder> submitProductOrder(SubmitedOrder submitedOrder, Long storeId){
        setWebClientBaseUri(productManagementBaseUrl);
        return webClient.post().uri(createOrderPath, storeId)
                        .bodyValue(submitedOrder)
                        .exchange()
                        .flatMap(response -> response.toEntity(ProductOrder.class))
                        .block();

    }

}