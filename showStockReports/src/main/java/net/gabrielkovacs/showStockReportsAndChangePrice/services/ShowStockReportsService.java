package net.gabrielkovacs.showStockReportsAndChangePrice.services;

import net.gabrielkovacs.showStockReportsAndChangePrice.entities.ReportEntry;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.StockItem;
import net.gabrielkovacs.showStockReportsAndChangePrice.entities.StockItemReport;
import net.gabrielkovacs.showStockReportsAndChangePrice.repository.StockItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowStockReportsService {

    Logger log = LoggerFactory.getLogger(ShowStockReportsService.class);
    private StockItemRepository stockItemRepository;

    public ShowStockReportsService(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public List<ReportEntry> retrieveStockItemReportForStore(Long storeId){
       List<StockItemReport> query = stockItemRepository.getReportDataForStore(storeId);
        List<ReportEntry> toReturn = query.stream().map(n -> mapStockItemReportToReportEntry(n)).collect(Collectors.toList());
        return toReturn;
    }

    private ReportEntry mapStockItemReportToReportEntry(StockItemReport stockItemReport){
        return new ReportEntry(stockItemReport.getID(),stockItemReport.getAmount(), stockItemReport.getMinStock(),
                stockItemReport.getMaxStock());
    }
/*
    // Should PUT return something?
    public void changeStockItemPrice(Long stockItemId, Double newSalesPrice){
        Optional<StockItem> queryResult = stockItemRepository.findById(stockItemId);
        queryResult.ifPresent(stockItem ->{stockItem.setSalePrice(newSalesPrice);
            stockItemRepository.save(stockItem);
        });

    }
*/
    public ResponseEntity<StockItem> changeStockItemPrice(Long stockItemId, Double newSalesPrice){
        Optional<StockItem> queryResult = stockItemRepository.findById(stockItemId);

        if(queryResult.isPresent()){
            StockItem temp = queryResult.get();
            temp.setSalePrice(newSalesPrice);
            return ResponseEntity.ok(stockItemRepository.save(temp));
        }else{
            return ResponseEntity.notFound().build();
        }

    }
}