package net.gabrielkovacs.apigateway.services;

import com.google.gson.Gson;
import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class ApiGatewayServices {

    Logger log = LoggerFactory.getLogger(ApiGatewayServices.class);

    private WebClient webClient;

    public void setWebClientBaseUri(String baseUri) {
        this.webClient = WebClient.create(baseUri);
    }

    public String generateCorrelationId(){
        return UUID.randomUUID().toString();
    }

    public String generateJSONStringFromClass(ClientCallBack clientCallBack){
        Gson gson = new Gson();
        return  gson.toJson(clientCallBack);
    }

    public ResponseEntity<ClientCallBack> sendDataToCallbackAddress(String callBackURL, ClientCallBack clientCallBack){
        setWebClientBaseUri(callBackURL);
        log.info("LAST HOP: Sending data to callback address: {}", clientCallBack.toString());
        return webClient.post().bodyValue(clientCallBack).exchange()
                .flatMap(response -> response.toEntity(ClientCallBack.class)).block();
    }
}