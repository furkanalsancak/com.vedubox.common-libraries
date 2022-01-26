// package com.vedubox.common.service;
//
// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.stereotype.Service;
//
// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.function.Function;
// import java.util.stream.Collectors;
//
// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class AuthenticationService {
//
//     @Value("${app.secretKey}")
//     private String appSecretKey;
//
//     private static int TEN_HOURS = 1000*60*60*10;
//
//
//     public String generateToken(UserDetails userDetails) {
//         Map<String, Object> claims = new HashMap();
//         claims.put("roles", userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toSet()));
//
//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setSubject(userDetails.getUsername())
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .setExpiration(new Date(System.currentTimeMillis() + TEN_HOURS))
//                 .signWith(SignatureAlgorithm.HS256, appSecretKey)
//                 .compact();
//     }
//     public Boolean isValidToken(String token, UserDetails userDetails) {
//         final String username = extractUsername(token);
//         return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//     }
//
//     public String extractUsername(String token) {
//         return extractClaim(token, Claims::getSubject);
//     }
//
//     private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//         final Claims claims = extractAllClaims(token);
//         return claimsResolver.apply(claims);
//     }
//     private Claims extractAllClaims(String token) {
//         return Jwts.parser()
//                 .setSigningKey(appSecretKey)
//                 .parseClaimsJws(token)
//                 .getBody();
//     }
//     private Boolean isTokenExpired(String token) {
//         return extractClaim(token, Claims::getExpiration)
//                 .before(new Date());
//     }
// }
