package com.products_management.application.ports.input;

import com.products_management.application.dto.ProductSyncDto;

public interface IProductEventPort {
    void publishCreatedStockEvent(ProductSyncDto productSyncDto);

}
