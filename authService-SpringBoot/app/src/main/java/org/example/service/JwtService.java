package org.example.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    /*
    JWT-> header, payload, signature

    header->metadata about JWT
    payload-> claims, standard->issuer, user, expiration; public->roles, permissions;
    private->preferences, custom data
    */

    //using hmac, need to share with everyone who needs it to verify jwt
    //else we can use something rsa which uses public and private key, so no need to share
    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    //Claims->Claims.getSubject(); equivalent
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

        Signature being used to check if tampered with
     */
    /*
    User u=new User("a", "b)
    User.userName("a").password("b").build()->chaining
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
        Instant expirationTime = Instant.now().plusSeconds(3600);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
                //token signing key and algorithm, compact the jwt to string
    }

    public String generateToken(String username){
        Map<String, Object> claims=new HashMap<>();
        return createToken(claims, username);
    }
}
