package net.gabrielkovacs.showStockReportsAndChangePrice.consumer;

public enum OrderState {
    initiated, request_item_stock, computed_new_stock, updated_stock,finished_update
}
