package net.gabrielkovacs.showStockReportsAndChangePrice.entities;

public class ReportEntry {

    private long id;
    private int amount;
    private int minStock;
    private int maxStock;

    public ReportEntry(){}

    public ReportEntry(long id, int amount, int minStock, int maxStock) {
        this.id = id;
        this.amount = amount;
        this.minStock = minStock;
        this.maxStock = maxStock;
    }

    public long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getMinStock() {
        return minStock;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }
}
