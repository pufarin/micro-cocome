package net.gabrielkovacs.apigateway.models;

public class StockItemReport{
 
    private Long id;
    private Long amount;
    private Long minStock;
    private Long maxStock;
   
    public StockItemReport(){}

    public StockItemReport(Long id, Long amount, Long minStock, Long maxStock) {
        this.id = id;
        this.amount = amount;
        this.minStock = minStock;
        this.maxStock = maxStock;
      
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getMinStock() {
        return this.minStock;
    }

    public void setMinStock(Long minStock) {
        this.minStock = minStock;
    }

    public Long getMaxStock() {
        return this.maxStock;
    }

    public void setMaxStock(Long maxStock) {
        this.maxStock = maxStock;
    }



}