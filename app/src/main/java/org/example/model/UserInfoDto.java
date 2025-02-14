package org.example.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.example.entities.UserInfo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
public class UserInfoDto extends UserInfo {

    //check once as changed
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private Long phoneNumber;
    private String email;
}
