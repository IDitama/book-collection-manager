package com.book.collection.book_collection_manager.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${rabbitmq.queue}")
    private String queueName;

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    @Bean
    public Queue booksQueue() {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public TopicExchange booksExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue booksQueue, TopicExchange booksExchange) {
        return BindingBuilder.bind(booksQueue).to(booksExchange).with(routingKey);
    }
}
