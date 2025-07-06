package com.products_management.infraestructure.output.messageBroker;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.products_management.application.ports.input.IStockEventPort;
import com.products_management.infraestructure.config.RabbitConfig;
import com.products_management.infraestructure.output.messageBroker.dto.EventDto;
import com.products_management.infraestructure.output.messageBroker.dto.StockDto;
import com.products_management.infraestructure.output.messageBroker.enums.EventType;
import com.products_management.infraestructure.security.IJwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockEventPublisher implements IStockEventPort{

    private final RabbitTemplate rabbitTemplate;
    private final IJwtUtils jwtUtils;

    @Override
    public void publishCreatedStockEvent(Long productId) {
        EventDto<StockDto> event = new EventDto<>(EventType.CREATED, new StockDto(productId));
        log.info("Publishing stock created event for productId: {}", productId);

        rabbitTemplate.convertAndSend(RabbitConfig.PRODUCT_EXCHANGE, "", event, message -> {
            message.getMessageProperties().setHeader("x-tenant-id", jwtUtils.getId());
            return message;
        });
    }
    
}