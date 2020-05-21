package net.gabrielkovacs.showDeliveryReports.services;

import net.gabrielkovacs.showDeliveryReports.entities.*;
import net.gabrielkovacs.showDeliveryReports.flow.GenerateReportSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.SubmissionPublisher;

@Service
public class MessageHandler {
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private SubmissionPublisher<QueryResponse> publisher = new SubmissionPublisher<>();
    private GenerateReportSubscriber generateReportSubscriber;

    private MessageManipulation messageManipulation;
    private MessageProducer messageProducer;
    private GenerateReportService generateReportService;

    public MessageHandler(GenerateReportSubscriber generateReportSubscriber, MessageManipulation messageManipulation, MessageProducer messageProducer, GenerateReportService generateReportService) {
        this.generateReportSubscriber = generateReportSubscriber;
        this.messageManipulation = messageManipulation;
        this.messageProducer = messageProducer;
        this.generateReportService = generateReportService;
        publisher.subscribe(generateReportSubscriber);
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
                QueryResponse queryResponseReceivedOrder = new QueryResponse(responsePayload,serviceBusMessageResponse.getCorrelationId(), new Timestamp( date.getTime()));
                log.info("finished_update State: {}", queryResponseReceivedOrder.toString());
                publisher.submit(queryResponseReceivedOrder);
                break;
        }

    }
}
