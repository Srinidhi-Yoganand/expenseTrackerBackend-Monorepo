package org.example.eventProducer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEventDto {
    private String first_name;
    private String last_name;
    private String email;
    private Long phone_number;
    private String user_id;
}
