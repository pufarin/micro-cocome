package net.gabrielkovacs.apigateway.services;

import com.google.gson.Gson;
import net.gabrielkovacs.apigateway.entities.ClientCallBack;
import net.gabrielkovacs.apigateway.models.QueryResponse;
import net.gabrielkovacs.apigateway.repository.ClientCallBackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageHandler {

    private ClientCallBackRepository clientCallBackRepository;
    private ApiGatewayServices apiGatewayServices;
    private Gson g = new Gson();

    public MessageHandler(ClientCallBackRepository clientCallBackRepository, ApiGatewayServices apiGatewayServices) {
        this.clientCallBackRepository = clientCallBackRepository;
        this.apiGatewayServices = apiGatewayServices;
    }

    public ResponseEntity<ClientCallBack> sendDataToCallback(String callbackUrl, ClientCallBack clientCallBack ){
       return apiGatewayServices.sendDataToCallbackAddress(callbackUrl, clientCallBack);
    }

    public QueryResponse convertStringToQueryResponse(String message){
        return g.fromJson(message, QueryResponse.class);
    }

    public Optional<ClientCallBack> getClientCallbackById(String uuid){
        return  clientCallBackRepository.findById(uuid);
    }

}
