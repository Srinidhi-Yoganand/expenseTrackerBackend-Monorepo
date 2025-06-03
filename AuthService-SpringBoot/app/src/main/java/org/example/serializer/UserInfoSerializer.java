package org.example.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.example.eventProducer.UserEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

//if we had to send normal stuff we could've, but as we are using DTO we need to serialize
public class UserInfoSerializer implements Serializer<UserEventDto> {

    private static final Logger log = LoggerFactory.getLogger(UserInfoSerializer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String arg0, UserEventDto userInfoEvent) {
        byte[] retVal = null;
        try {
            System.out.println("Serializing UserInfoEvent: " + userInfoEvent.toString());
            // Serialize the UserInfoEvent to JSON bytes
            retVal = objectMapper.writeValueAsString(userInfoEvent).getBytes();
            System.out.println("Serialized message: " + new String(retVal));
        } catch (Exception e) {
            // Log the error to a logging framework or throw a runtime exception
            System.err.println("Error serializing UserInfoEvent: " + e.getMessage());
            e.printStackTrace();
        }
        return retVal;
    }

    @Override public void close() {
    }
}