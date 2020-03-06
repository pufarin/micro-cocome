package net.gabrielkovacs.showStockReportsAndChangePrice.entities;


public interface StockItemReport {

   long getID();
   int getAmount();
   int getMinStock();
   int getMaxStock();

}