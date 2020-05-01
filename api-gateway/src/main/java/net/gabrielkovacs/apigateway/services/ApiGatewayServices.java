package net.gabrielkovacs.apigateway.services;

import java.util.UUID;

import com.google.gson.Gson;
import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApiGatewayServices {

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
        return webClient.post().bodyValue(clientCallBack).exchange()
                .flatMap(response -> response.toEntity(ClientCallBack.class)).block();
    }
}