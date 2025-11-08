package com.products_management.infraestructure.output.messageBroker;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.products_management.application.ports.input.IProductUsagePort;
import com.products_management.infraestructure.config.rabbitConfig.RabbitProductConfig;
import com.products_management.infraestructure.output.messageBroker.dto.EventDto;
import com.products_management.infraestructure.output.messageBroker.dto.ProductUsageEventDto;
import com.products_management.infraestructure.output.messageBroker.enums.EventUsageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @brief Listener para eventos de uso de productos desde PEPS
 *
 * Escucha eventos de RabbitMQ cuando PEPS notifica que ha utilizado un producto,
 * actualizando el contador de uso correspondiente.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductUsageListener {

    private final IProductUsagePort productUsagePort;

    /**
     * @brief Maneja eventos de uso de productos desde la cola
     * @param event Evento con información del producto usado
     */
    @RabbitListener(queues = RabbitProductConfig.PRODUCT_USAGE_QUEUE)
    public void handleProductUsageEvent(EventDto<ProductUsageEventDto> event) {
        log.info("Received product usage event");
        
        try {
            if (!isValidEvent(event)) {
                log.warn("Invalid product usage event received");
                return;
            }
            
            ProductUsageEventDto data = event.getData();
            log.info("Processing usage for productId: {}, quantity: {}", 
                     data.getProductId(), data.getQuantityUsed());
            
            productUsagePort.incrementUsageCount(data.getProductId(), data.getEnterpriseId());
            
            log.info("Product usage event processed successfully for productId: {}", data.getProductId());
            
        } catch (Exception e) {
            log.error("Error processing product usage event: {}", e.getMessage(), e);
            // En caso de error, el mensaje se pierde intencionalmente para no bloquear la cola
            // Se podría implementar DLQ o reintentos según necesidades del negocio
        }
    }

    /**
     * @brief Valida la integridad del evento recibido
     * @param event Evento a validar
     * @return true si el evento es válido
     */
    private boolean isValidEvent(EventDto<ProductUsageEventDto> event) {
        if (event == null) {
            log.warn("Event is null");
            return false;
        }
        
        if (event.getData() == null) {
            log.warn("Event data is null");
            return false;
        }
        
        ProductUsageEventDto data = event.getData();
        
        if (data.getProductId() == null) {
            log.warn("ProductId is null - required field");
            return false;
        }
        
        if (data.getEnterpriseId() == null || data.getEnterpriseId().trim().isEmpty()) {
            log.warn("EnterpriseId is null or empty - required field");
            return false;
        }
        
        if (data.getQuantityUsed() == null || data.getQuantityUsed() <= 0) {
            log.warn("QuantityUsed is null or invalid - required field");
            return false;
        }
        
        return true;
    }
}

