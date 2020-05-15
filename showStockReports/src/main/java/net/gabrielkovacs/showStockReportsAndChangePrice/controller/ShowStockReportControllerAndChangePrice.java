package net.gabrielkovacs.showStockReportsAndChangePrice.controller;

import io.swagger.v3.oas.annotations.Operation;
import net.gabrielkovacs.common.entities.StockItem;
import net.gabrielkovacs.common.entities.StockItemReport;
import net.gabrielkovacs.common.repository.StockItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
class ShowStockReportControllerAndChangePrice {
    
    private StockItemRepository stockItemRepository;

    
    public ShowStockReportControllerAndChangePrice(StockItemRepository stockItemReportRepository){
        this.stockItemRepository = stockItemReportRepository;
    }
    
    @Operation(summary = "UC5 Show Stock Reports", description = "Returns a Stock Item Report for a given store id")
    @GetMapping("stockitemreport/{storeId}")
    public ResponseEntity<List<StockItemReport>> getStoreItemReport(@PathVariable long storeId){

        List<StockItemReport> stockItemReports = stockItemRepository.getReportDataForStore(storeId);
        if(stockItemReports.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok().body(stockItemReports);
        }
    }

    @GetMapping("stockitem/store/{storeId}")
    public ResponseEntity<List<StockItem>> getStockItemByStoreId(@PathVariable long storeId){

        List<StockItem> queryResult = stockItemRepository.findAllByStoreId(storeId);
        if(queryResult.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(queryResult);
    }

    @Operation(summary = "UC7 Change Stock Item Price ", description = "Updates the price of a stockItem based on the provided data")
    @PutMapping("stockitem/store/{storeId}/{stockItemId}")
    public ResponseEntity<?> updatePrice(@RequestBody StockItem newStockItem, @PathVariable Long storeId, @PathVariable Long stockItemId){
        Optional<StockItem> queryResult = stockItemRepository.findById(stockItemId);
        queryResult.ifPresent(stockItem ->{stockItem.setSalePrice(newStockItem.getSalePrice());
            stockItemRepository.save(stockItem);
        });
        return ResponseEntity.ok().build();
    }
}