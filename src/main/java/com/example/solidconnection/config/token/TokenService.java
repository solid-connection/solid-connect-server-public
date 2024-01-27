package com.example.solidconnection.config.token;

import com.example.solidconnection.custom.exception.CustomException;
import com.example.solidconnection.siteuser.repository.SiteUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.example.solidconnection.custom.exception.ErrorCode.EMAIL_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class TokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private final SiteUserRepository siteUserRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String email, TokenType tokenType) {
        Claims claims = Jwts.claims().setSubject(email);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + tokenType.getExpireTime());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    public String saveToken(String email, TokenType tokenType) {
        String token = generateToken(email, tokenType);

        redisTemplate.opsForValue().set(
                tokenType.getPrefix() + email,
                token,
                tokenType.getExpireTime(),
                TimeUnit.MILLISECONDS
        );
        return token;
    }

    public Authentication getAuthentication(String token) {
        String email = getClaim(token).getSubject();
        UserDetails userDetails = (UserDetails) siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims getClaim(String token) {
        return Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}