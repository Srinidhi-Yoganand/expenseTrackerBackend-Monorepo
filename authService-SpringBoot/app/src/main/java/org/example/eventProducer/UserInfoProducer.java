package org.example.eventProducer;

import org.example.model.UserInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import static org.example.service.UserDetailsServiceImpl.log;

@Service
public class UserInfoProducer {
    private static final Logger log = LoggerFactory.getLogger(UserInfoProducer.class);
    private final KafkaTemplate<String, UserEventDto> kafkaTemplate;

    @Value("${spring.kafka.topic-json.name}")
    private String topicJsonName;

    UserInfoProducer(KafkaTemplate<String, UserEventDto> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoEvent eventData){
        UserEventDto eventDto = eventData.toDto();

        System.out.println("Sending message to Kafka topic: " + topicJsonName);
        System.out.println("Sending message to Kafka topic: " + eventDto.toString());

        Message<UserEventDto> message = MessageBuilder.withPayload(eventDto)
                .setHeader(KafkaHeaders.TOPIC, topicJsonName)
                .build();
        kafkaTemplate.send(message);
    }
}
