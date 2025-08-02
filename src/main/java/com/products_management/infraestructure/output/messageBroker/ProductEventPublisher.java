package com.products_management.infraestructure.output.messageBroker;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.products_management.application.dto.ProductSyncDto;
import com.products_management.application.ports.input.IProductEventPort;
import com.products_management.infraestructure.config.RabbitConfig;
import com.products_management.infraestructure.output.messageBroker.dto.EventDto;
import com.products_management.infraestructure.output.messageBroker.enums.EventType;
import com.products_management.infraestructure.security.IJwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher implements IProductEventPort{

    private final RabbitTemplate rabbitTemplate;
    private final IJwtUtils jwtUtils;

    @Override
    public void publishCreatedStockEvent(ProductSyncDto productSyncDto) {
        EventDto<ProductSyncDto> event = new EventDto<>(EventType.CREATED, productSyncDto);
        log.info("Publishing stock created event for product: {}", productSyncDto);

        rabbitTemplate.convertAndSend(RabbitConfig.PRODUCT_EXCHANGE, "", event, message -> {
            message.getMessageProperties().setHeader("x-tenant-id", jwtUtils.getId());
            return message;
        });
    }
    
}