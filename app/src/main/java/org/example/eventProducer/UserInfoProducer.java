package org.example.eventProducer;

import org.example.model.UserInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class UserInfoProducer {
    private final KafkaTemplate<String, UserInfoDto> kafkaTemplate;

    @Value("${spring.kafka.topic-json.name}")
    private String topicJsonName;

    UserInfoProducer(KafkaTemplate<String, UserInfoDto> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void sendEventToKafka(UserInfoEvent eventData){
        Message<UserInfoEvent> message = MessageBuilder.withPayload(eventData)
                .setHeader(KafkaHeaders.TOPIC, topicJsonName).build();
        kafkaTemplate.send(message);
    }
}
