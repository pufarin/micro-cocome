package net.gabrielkovacs.showStockReportsAndChangePrice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface StockItemReport {

    long getID();
    @JsonProperty("productName")
    String getName();
    int getAmount();
    int getMinStock();
    int getMaxStock(); 

}