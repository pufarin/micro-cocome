package net.gabrielkovacs.showStockReportsAndChangePrice.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.*;
import net.gabrielkovacs.showStockReportsAndChangePrice.repository.ServiceBusRepository;
import net.gabrielkovacs.showStockReportsAndChangePrice.repository.StockItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static net.gabrielkovacs.showStockReportsAndChangePrice.consumer.OrderState.request_item_stock;
import static net.gabrielkovacs.showStockReportsAndChangePrice.consumer.OrderState.updated_stock;

@Service
public class MessageHandler {
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private MessageManipulation messageManipulation;
    private ShowStockReportsService showStockReportsService;
    private MessageProducer messageProducer;
    private StockItemRepository stockItemRepository;
    private ServiceBusRepository serviceBusRepository;

    public MessageHandler(MessageManipulation messageManipulation, ShowStockReportsService showStockReportsService,
                          MessageProducer messageProducer, StockItemRepository stockItemRepository,
                          ServiceBusRepository serviceBusRepository) {
        this.messageManipulation = messageManipulation;
        this.showStockReportsService = showStockReportsService;
        this.messageProducer = messageProducer;
        this.stockItemRepository = stockItemRepository;
        this.serviceBusRepository =  serviceBusRepository;
    }

    public void cosumeMessage(ClientCallBack clientCallBack){
        log.info("I am in the handler,  {}", clientCallBack.toString());
        String eventName = clientCallBack.getEventName();
        Date date= new Date();
        switch (eventName){
            case("returnStockItemReports"):
                log.info("Already in the returnStockItemReports {}", eventName);

                List<ReportEntry> reportEntries = showStockReportsService.retrieveStockItemReportForStore(Long.parseLong(clientCallBack.getParameter()));
                String responsePayload = messageManipulation.convertListOfReportEntryToString(reportEntries);
                QueryResponse queryResponse = new QueryResponse(responsePayload,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                log.info("This is the query response {} ", queryResponse.toString());
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse));
               // log.info("This is the query response as string {} ",messageManipulation.convertQueryResponseToString(queryResponse));

                break;
            case("changeStockItemPrice"):
                log.info("Already in the changeStockItemPrice {}", clientCallBack.toString());
                StockItem stockItem = messageManipulation.convertStringToStockItemObject(clientCallBack.getParameter());
                ResponseEntity<StockItem> responseEntity = showStockReportsService.changeStockItemPrice(stockItem.getId(), stockItem.getSalePrice());
                log.info("This is the ResponseEntity {}:", responseEntity );
                String responsePayload1 = messageManipulation.convertResponseEntityToString(responseEntity);
                QueryResponse queryResponse1 = new QueryResponse(responsePayload1,clientCallBack.getUuid(),new Timestamp( date.getTime()));
                log.info("This is the query response {} ", queryResponse1.toString());
                messageProducer.sendMessageToApiGateway(messageManipulation.convertQueryResponseToString(queryResponse1));

                break;
        }
    }

    public void consumeServiceBusMessageCommand(ServiceBusMessageCommand serviceBusMessageCommand){
        log.info("I am in the ServiceBusMessageCommand handler,  {}", serviceBusMessageCommand.toString());
        serviceBusRepository.save(serviceBusMessageCommand);
        String commandData = serviceBusMessageCommand.getCommand();
        JsonObject command = new Gson().fromJson(commandData, JsonObject.class);
        String methodName = command.get("method").getAsString();
        Date date= new Date();
        switch(methodName){
            case("getStockItemByStoreIdAndProductId"):

                log.info("This is the getStockItemByStoreIdAndProductId CASE");
                Timestamp theTimeStamp = new Timestamp( date.getTime());

                Optional<StockItem> queryResult = stockItemRepository.findAllByStoreIdAndProductId(
                        command.get("storeId").getAsLong(),command.get("productId").getAsLong());

                if(queryResult.isPresent()){
                    StockItem stockItem = queryResult.get();
                    ServiceBusMessageResponse serviceBusMessageResponse = new ServiceBusMessageResponse(
                            serviceBusMessageCommand.getCorrelationId(),
                            messageManipulation.convertStockItemToString(stockItem),
                            theTimeStamp,"SR",request_item_stock.name());
                    messageProducer.sendMessageToServiceBusResponse(messageManipulation.convertServiceBusMessageResponseToString(serviceBusMessageResponse));

                }else{
                    ServiceBusMessageResponse serviceBusMessageResponse = new ServiceBusMessageResponse(
                            serviceBusMessageCommand.getCorrelationId(),
                            messageManipulation.convertStockItemToString(new StockItem()),
                            theTimeStamp,"SR",request_item_stock.name());
                    messageProducer.sendMessageToServiceBusResponse(messageManipulation.convertServiceBusMessageResponseToString(serviceBusMessageResponse));
                }
                log.info("This is the StockItem: {}", queryResult.toString());
                break;
            case("updateStockItem"):
                log.info("This is the updateStockItem CASE {}", serviceBusMessageCommand.toString());
                Timestamp theTimeStamp1 = new Timestamp( date.getTime());

                String stockItemString = command.get("stockItem").getAsString();
                log.info("This is the STOCK ITEM: {}", stockItemString);
                StockItem stockItem = messageManipulation.convertStringToStockItemObject(stockItemString);
                stockItemRepository.save(stockItem);

                ServiceBusMessageResponse serviceBusMessageResponse = new ServiceBusMessageResponse(
                        serviceBusMessageCommand.getCorrelationId(),
                        messageManipulation.convertStockItemToString(stockItem),
                        theTimeStamp1,"SR",updated_stock.name());
                messageProducer.sendMessageToServiceBusResponse(messageManipulation.convertServiceBusMessageResponseToString(serviceBusMessageResponse));
                break;


        }





    }
}
