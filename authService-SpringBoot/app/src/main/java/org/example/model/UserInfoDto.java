package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.example.entities.UserInfo;

import java.util.HashSet;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    @NonNull
    @JsonProperty("firstName")
    private String firstName;
    @NonNull
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("phoneNumber")
    private Long phoneNumber;
    @JsonProperty("email")
    private String email;

    @Override
    public String toString() {
        return "UserInfoDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", email='" + email + '\'' +
                '}';
    }
}
