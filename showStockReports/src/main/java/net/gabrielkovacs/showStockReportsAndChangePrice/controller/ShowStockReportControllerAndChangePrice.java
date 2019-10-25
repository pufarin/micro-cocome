package net.gabrielkovacs.showStockReportsAndChangePrice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.gabrielkovacs.showStockReportsAndChangePrice.entities.StockItem;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.StockItemReport;
import net.gabrielkovacs.showStockReportsAndChangePrice.repository.StockItemRepository;


@RestController
class ShowStockReportControllerAndChangePrice {
    
    private StockItemRepository stockItemRepository;

    
    public ShowStockReportControllerAndChangePrice(StockItemRepository stockItemReportRepository){
        this.stockItemRepository = stockItemReportRepository;
    }
    
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

    @GetMapping("stockitem")
    public ResponseEntity<StockItem> getStockItemByStoreIdAndProductId(@RequestParam long storeId, @RequestParam long productId){

        Optional<StockItem> queryResult = stockItemRepository.findAllByStoreIdAndProductId(storeId,productId);

        if(queryResult.isPresent()){
            return ResponseEntity.ok().body(queryResult.get());

        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("stockitem/{stockItemId}")
    public ResponseEntity<?> updateStockItem(@RequestBody StockItem stockItem, @PathVariable Long stockItemId){

        Optional<StockItem> queryResult = stockItemRepository.findById(stockItemId);

        if(queryResult.isPresent() && stockItem.getId() == stockItemId){
            stockItemRepository.save(stockItem);
            return ResponseEntity.ok().build();

        }else{
            return ResponseEntity.notFound().build();
        }

    }



    @PutMapping("stockitem/store/{storeId}/{stockItemId}")
    void updatePrice(@RequestBody StockItem newStockItem, @PathVariable Long storeId, @PathVariable Long stockItemId){
        Optional<StockItem> queryResult = stockItemRepository.findById(stockItemId);
        queryResult.ifPresent(stockItem ->{stockItem.setSalePrice(newStockItem.getSalePrice());
            stockItemRepository.save(stockItem);
        });
    }
}