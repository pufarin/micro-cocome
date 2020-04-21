package net.gabrielkovacs.showDeliveryReports.services;

import net.gabrielkovacs.showDeliveryReports.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class MessageHandler {
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private MessageManipulation messageManipulation;
    private MessageProducer messageProducer;
    private GenerateReportService generateReportService;

    public MessageHandler(MessageManipulation messageManipulation, MessageProducer messageProducer, GenerateReportService generateReportService) {
        this.messageManipulation = messageManipulation;
        this.messageProducer = messageProducer;
        this.generateReportService = generateReportService;
    }

    public void cosumeMessage(ClientCallBack clientCallBack){
        log.info("I am in the handler,  {}", clientCallBack.toString());
        String eventName = clientCallBack.getEventName();
        Date date= new Date();
        switch (eventName){
            case("returnDeliveryReports"):
                Timestamp theTimeStamp = new Timestamp( date.getTime());
                log.info("Already in the returnDeliveryReports {}", eventName);

                Long enterpriseId = Long.parseLong(clientCallBack.getParameter());

                ProductSupplierAndProducts productSupplierAndProducts = generateReportService.generateProductSupplierAndProducts(enterpriseId);

                if(productSupplierAndProducts.getSupplyChain().isEmpty()) {
                    String responsePayload = String.format("The enterprise %d has no registered suppliers", enterpriseId);
                    sendReplyToApiGateway(clientCallBack.getUuid(), date, responsePayload);
                }else{
                    ServiceBusMessageCommand getDeliveryTimePerProductIdCommand = generateReportService.generateGetDeliveryTimePerProductCommandMessage(
                            clientCallBack.getUuid(),productSupplierAndProducts, theTimeStamp, "dr");
                    messageProducer.getDeliveryTimePerProductId(getDeliveryTimePerProductIdCommand);
                }
                break;
            case("placeholder"):
                log.info("Already in the placeholder {}", clientCallBack.toString());

                break;
        }
    }

    private void sendReplyToApiGateway(String correlationId, Date date, String responsePayload) {

        QueryResponse queryResponse = new QueryResponse(responsePayload, correlationId,new Timestamp( date.getTime()));
        log.info("This is the query response {} ", queryResponse.toString());
        messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse));
    }

    public void consumeServiceBusMessageResponse(ServiceBusMessageResponse serviceBusMessageResponse){
        log.info("I am in the ServiceBusMessageResponse handler,  {}", serviceBusMessageResponse.toString());
        String eventName = serviceBusMessageResponse.getEventName();
        Date date= new Date();
        switch (eventName){
            case("getDeliveryDurationPerProduct"):
                ProductSupplierAndProducts productSupplierAndProducts = messageManipulation.jsonStringToProductSupplierAndProducts(serviceBusMessageResponse.getDocument());
                List<DeliveryReport> response = generateReportService.generateDeliveryReport(productSupplierAndProducts);
                String responsePayload = messageManipulation.convertListOfDeliveryReportsToString(response);

                sendReplyToApiGateway(serviceBusMessageResponse.getCorrelationId(),date,responsePayload);
                break;
        }

    }
}
