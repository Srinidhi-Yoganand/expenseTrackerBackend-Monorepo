package org.example.controller;

import org.checkerframework.checker.units.qual.A;
import org.example.entities.RefreshToken;
import org.example.entities.UserInfo;
import org.example.repository.UserRepository;
import org.example.request.AuthRequestDTO;
import org.example.request.RefreshTokenRequestDTO;
import org.example.response.JwtResponseDTO;
import org.example.service.JwtService;
import org.example.service.RefreshTokenService;
import org.example.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Controller
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenService refreshTokenRepository;

    @PostMapping("auth/v1/login")
    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            String userId = userDetailsService.getUserByUsername(authRequestDTO.getUsername());

            if(Objects.nonNull(userId) && Objects.nonNull(refreshToken)){
                return new ResponseEntity<>(JwtResponseDTO.builder()
                        .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
                        .token(refreshToken.getToken())
                        .userId(userId)
                        .build(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("auth/v1/refreshToken")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        String refreshToken = refreshTokenRequestDTO.getToken();
//        System.out.println("Received refresh token: " + refreshToken);

        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshToken);

        if (optionalRefreshToken.isPresent()) {
//            System.out.println("Found refresh token in DB.");

            RefreshToken refreshTokenEntity = optionalRefreshToken.get();

            try {
                refreshTokenService.verifyExpiration(refreshTokenEntity);
//                System.out.println("Refresh token is valid and not expired.");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                throw e; // Handle expired refresh token error
            }

            UserInfo userInfo = refreshTokenEntity.getUserInfo();
//            System.out.println("User info retrieved for username: " + userInfo.getUsername());

            String accessToken = jwtService.generateToken(userInfo.getUsername());
//            System.out.println("Generated new access token for user: " + userInfo.getUsername());
//            System.out.println("Access token: " + accessToken);

            return ResponseEntity.ok(JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .token(refreshTokenEntity.getToken())
                    .userId(userInfo.getUserId())
                    .build());
        } else {
//            System.out.println("Refresh token not found in DB.");
            throw new RuntimeException("Refresh Token is not in DB..!!");
        }
    }
}
