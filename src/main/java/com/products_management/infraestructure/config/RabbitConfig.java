package com.products_management.infraestructure.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;

@Configuration
@Slf4j
public class RabbitConfig {
    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String PRODUCT_STOCK_QUEUE = "product.stock.queue";
    public static final String PRODUCT_KARDEX_QUEUE = "product.kardex.queue";
    
    // Dead Letter Queue configuration
    public static final String PRODUCT_STOCK_DLQ = "product.stock.dlq";
    public static final String PRODUCT_KARDEX_DLQ = "product.kardex.dlq";
    public static final String PRODUCT_DLX = "product.dlx";

    @Bean
    Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Dead Letter Exchange
    @Bean
    FanoutExchange productDlx() {
        return new FanoutExchange(PRODUCT_DLX, true, false);
    }

    // Dead Letter Queues
    @Bean
    Queue productStockDlq() {
        return QueueBuilder.durable(PRODUCT_STOCK_DLQ).build();
    }

    @Bean
    Queue productKardexDlq() {
        return QueueBuilder.durable(PRODUCT_KARDEX_DLQ).build();
    }

    // Main Queues with DLQ configuration
    @Bean
    Queue productStockQueue1() {
        return QueueBuilder.durable(PRODUCT_STOCK_QUEUE)
                .withArgument("x-dead-letter-exchange", PRODUCT_DLX)
                .withArgument("x-dead-letter-routing-key", "stock.dlq")
                .build();
    }

    @Bean
    Queue productKardexQueue() {
        return QueueBuilder.durable(PRODUCT_KARDEX_QUEUE)
                .withArgument("x-dead-letter-exchange", PRODUCT_DLX)
                .withArgument("x-dead-letter-routing-key", "kardex.dlq")
                .build();
    }

    @Bean
    FanoutExchange productExchange() {
        return new FanoutExchange(PRODUCT_EXCHANGE, true, false);
    }

    @Bean
    Binding productStockQueueBinding1() {
        return BindingBuilder.bind(productStockQueue1()).to(productExchange());
    }

    @Bean
    Binding productKardexQueueBinding() {
        return BindingBuilder.bind(productKardexQueue()).to(productExchange());
    }

    @Bean
    Binding productStockDlqBinding() {
        return BindingBuilder.bind(productStockDlq()).to(productDlx());
    }

    @Bean
    Binding productKardexDlqBinding() {
        return BindingBuilder.bind(productKardexDlq()).to(productDlx());
    }

    // Custom RabbitTemplate with proper error handling
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setMandatory(true);
        
        // Return callback para mensajes no enrutables
        template.setReturnsCallback(returned -> {
            log.error("Message returned: {}", returned.getMessage());
            log.error("Reply code: {}", returned.getReplyCode());
            log.error("Reply text: {}", returned.getReplyText());
            log.error("Exchange: {}", returned.getExchange());
            log.error("Routing key: {}", returned.getRoutingKey());
        });
        
        // Confirm callback para publishers
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error("Message not delivered to exchange. Cause: {}", cause);
            }
        });
        
        return template;
    }

    // Custom listener container factory
    @Bean
    RabbitListenerContainerFactory<SimpleMessageListenerContainer> rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
