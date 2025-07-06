package com.products_management.infraestructure.output.messageBroker.dto;

import com.products_management.infraestructure.output.messageBroker.enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto<T> {
    private EventType type;
    private T data;
}
