package org.example.service;

import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.repository.RefreshTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    //autowired->automatic dependency injection
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        UserInfo user=userRepository.findByUsername(username);

        Instant nowUtc = Instant.now();
//        System.out.println("Current UTC time: " + nowUtc);

        Instant expiryDate = nowUtc.plusSeconds(604800); // 7 days
//        System.out.println("Token will expire at: " + expiryDate);

        RefreshToken existingToken = refreshTokenRepository.findByUserInfo(user);
//        System.out.println("Existing token: " + existingToken);

        if (existingToken != null) {
            if (existingToken.getExpiryDate().isBefore(nowUtc)) {
//                System.out.println("Existing refresh token expired. Deleting the old token.");
                refreshTokenRepository.delete(existingToken);
            } else {
//                System.out.println("Existing refresh token is still valid.");
                return existingToken;
            }
        }

        String newToken = UUID.randomUUID().toString();
        Optional<RefreshToken> duplicateCheck = refreshTokenRepository.findByToken(newToken);
        while (duplicateCheck.isPresent()) {
            newToken = UUID.randomUUID().toString();
            duplicateCheck = refreshTokenRepository.findByToken(newToken);
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(user)
                .token(newToken)
                .expiryDate(expiryDate)
                .build();


        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token expired, please login again");
        }
        return token;
    }


    public RefreshToken getRefreshTokenByUserId(String userId) {
        UserInfo user = userRepository.findByUserId(userId);
        if (user != null) {
            return refreshTokenRepository.findByToken(user.getUsername()).orElse(null);
        }
        return null;
    }
}
