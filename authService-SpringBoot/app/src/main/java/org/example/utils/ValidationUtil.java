package org.example.utils;

import org.checkerframework.checker.units.qual.C;
import org.example.model.UserInfoDto;

public class ValidationUtil {

    public static void validateUserAttributes(UserInfoDto userInfoDto){
        if(userInfoDto==null)
            throw new IllegalArgumentException("User Info cannot be null");

        if(userInfoDto.getEmail()==null || userInfoDto.getEmail().isEmpty())
            throw new IllegalArgumentException("Email cannot be empty");

        if(!isValidEmail(userInfoDto.getEmail()))
            throw new IllegalArgumentException("Email format inavlid");

        if(userInfoDto.getPassword()==null || userInfoDto.getPassword().isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");

        if(userInfoDto.getPassword().length()<8)
            throw new IllegalArgumentException("Password must be atleast 8 characters long");

        if(!containsUppercase(userInfoDto.getPassword()))
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");

        if(!containsDigit(userInfoDto.getPassword()))
            throw new IllegalArgumentException("Password must contain at least one number");
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    private static boolean containsUppercase(String password){
        return password.chars().anyMatch(Character::isUpperCase);
    }

    private static boolean containsDigit(String password){
        return password.chars().anyMatch(Character::isDigit);
    }
}
