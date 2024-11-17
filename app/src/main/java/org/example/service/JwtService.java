package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*
        passed the reference to getExpiration method of claims object

        extract claim then get all claims and stores in Claims object
        the getExpiration method is run on Claims object to retrieve the data
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*
        T-> return type generic, determined at runtime
        Function<Claims, T> -> functional interface which takes claims object and return value of type T
        .apply -> Applies on R and returns it in type T

        This method takes a token, extracts the claims from the token,
        applies the provided claimsResolver function on the Claims object,
        and returns the result in the specified type T.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /*
        creates a jwt parser, sets a signing key to verify and completes the construction
        (builder pattern is used to create chaining, used instead of a large constructor)
        Parses jwt and verifies the signature, extracts the claims and returns as Claim object
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey(){
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String Token){
        //if expiration date is before date now
        return extractExpiration(Token).before(new Date());
    }

    public boolean validateToken(String Token, UserDetails userDetails){
        final String username=extractUsername(Token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(Token));
    }

    private String createToken(Map<String, Object> claims, String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+100000*60*1))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
                //token signing key and algorithm, compact the jwt to string
    }

    public String generateToken(String username){
        Map<String, Object> claims=new HashMap<>();
        return createToken(claims, username);
    }
}
