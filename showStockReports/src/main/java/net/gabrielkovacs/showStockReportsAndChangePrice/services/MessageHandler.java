package net.gabrielkovacs.showStockReportsAndChangePrice.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ServiceBusMessageCommand;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ServiceBusMessageResponse;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.StockItem;
import net.gabrielkovacs.showStockReportsAndChangePrice.repository.ServiceBusRepository;
import net.gabrielkovacs.showStockReportsAndChangePrice.repository.StockItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import static net.gabrielkovacs.showStockReportsAndChangePrice.consumer.OrderState.request_item_stock;
import static net.gabrielkovacs.showStockReportsAndChangePrice.consumer.OrderState.updated_stock;

@Service
public class MessageHandler {
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private MessageManipulation messageManipulation;
    private MessageProducer messageProducer;
    private StockItemRepository stockItemRepository;
    private ServiceBusRepository serviceBusRepository;

    public MessageHandler(MessageManipulation messageManipulation,
                          MessageProducer messageProducer, StockItemRepository stockItemRepository,
                          ServiceBusRepository serviceBusRepository) {
        this.messageManipulation = messageManipulation;
        this.messageProducer = messageProducer;
        this.stockItemRepository = stockItemRepository;
        this.serviceBusRepository =  serviceBusRepository;
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
