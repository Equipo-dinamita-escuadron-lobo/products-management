package com.products_management.application.ports.input;

public interface IStockEventPort {
    void publishCreatedStockEvent(Long productId);

}
