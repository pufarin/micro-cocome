package net.gabrielkovacs.common.entities;

public interface StockItemReport {

    long getID();
   // @JsonProperty("productName")
   // String getName();
    int getAmount();
    int getMinStock();
    int getMaxStock(); 

}