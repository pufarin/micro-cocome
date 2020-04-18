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
                log.info("Already in the returnDeliveryReports {}", eventName);

                Long enterpriseId = Long.parseLong(clientCallBack.getParameter());
                List<DeliveryReport> deliveryReports = generateReportService.generateDeliveryReport(enterpriseId);

                String responsePayload = messageManipulation.convertListOfDeliveryReportsToString(deliveryReports);
                QueryResponse queryResponse = new QueryResponse(responsePayload,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                log.info("This is the query response {} ", queryResponse.toString());

                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse));

                break;
            case("placeholder"):
                log.info("Already in the placeholder {}", clientCallBack.toString());

                break;
        }
    }

    public void consumeServiceBusMessageResponse(ServiceBusMessageResponse serviceBusMessageResponse){
        log.info("I am in the ServiceBusMessageResponse handler,  {}", serviceBusMessageResponse.toString());
        String eventName = serviceBusMessageResponse.getEventName();
        Date date= new Date();
        switch (eventName){
            case("getDeliveryDurationPerProduct"):
                List<ProductDeliveryDuration> productDeliveryDurations = messageManipulation.convertJsonToProductDeliveryDurations(serviceBusMessageResponse.getDocument());
                List<DeliveryReport> deliveryReports = productDeliveryDurations.stream().map()

                break;
        }

    }
}
