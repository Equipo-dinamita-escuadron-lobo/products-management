package com.products_management.infraestructure.output.messageBroker;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.products_management.infraestructure.config.rabbitConfig.RabbitProductUsedConfig;
import com.products_management.infraestructure.output.messageBroker.base.AbstractMessageListener;
import com.products_management.infraestructure.output.messageBroker.dto.EventDto;
import com.products_management.infraestructure.output.messageBroker.enums.EventType;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @brief RabbitMQ listener for product synchronization events
 * 
 * Handles product lifecycle events (create, update, delete) from message broker
 * with error handling and recovery mechanisms for reliable data synchronization.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductListener extends AbstractMessageListener<EventDto<Boolean>> {


    @RabbitListener(queues = RabbitProductUsedConfig.PRODUCT_USED_QUEUE)
        public void handleProductEvent(
                EventDto<Boolean> event,
                Message message, 
                Channel channel,
                @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
            
            handleMessage(event, channel, deliveryTag);
    }


    @Override
    protected void processEvent(EventDto<Boolean> event) {
        log.info("Processing product used event, isItBeingUsed: {}", event.getData());
        if (event.getType() == EventType.USED) {
            // Process the event accordingly
            log.info("Contador + 1 :v ");
            boolean isItBeingUsed = event.getData();
            // Here you would typically update your product's "used" status in the database
            log.info("Product used status updated to: {}", isItBeingUsed);
        } else {
            log.warn("Received unsupported event type: {}", event.getType());
        }
    }

    @Override
    protected boolean isValidEvent(EventDto<Boolean> event) {
        return true;
    }

    @Override
    protected String getEntityType() {
        return "ProductUsed";
    }

}


