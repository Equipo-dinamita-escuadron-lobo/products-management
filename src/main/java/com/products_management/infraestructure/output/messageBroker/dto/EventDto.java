package com.products_management.infraestructure.output.messageBroker.dto;

import com.products_management.infraestructure.output.messageBroker.enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @brief DTO genérico para eventos de message broker
 *
 * Estructura genérica que encapsula eventos con tipo de operación
 * y datos asociados para comunicación vía RabbitMQ.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto<T> {
    private EventType type;
    private T data;
}
