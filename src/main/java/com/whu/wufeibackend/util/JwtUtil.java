package com.whu.wufeibackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和验证JWT令牌
 */
@Component
public class JwtUtil {
    
    /**
     * JWT密钥
     */
    @Value("${jwt.secret}")
    private String secret;
    
    /**
     * JWT过期时间（毫秒）
     */
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * 生成JWT密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    /**
     * 生成JWT令牌
     * 
     * @param username 用户名
     * @param userType 用户类型
     * @return JWT令牌
     */
    public String generateToken(String username, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("userType", userType);
        return createToken(claims, username);
    }
    
    /**
     * 创建JWT令牌
     * 
     * @param claims 声明
     * @param subject 主题
     * @return JWT令牌
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 从JWT令牌中获取用户名
     * 
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    /**
     * 从JWT令牌中获取用户类型
     * 
     * @param token JWT令牌
     * @return 用户类型
     */
    public String getUserTypeFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userType", String.class);
    }
    
    /**
     * 从JWT令牌中获取过期时间
     * 
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    /**
     * 从JWT令牌中获取指定声明
     * 
     * @param token JWT令牌
     * @param claimsResolver 声明解析器
     * @return 声明值
     */
    public <T> T getClaimFromToken(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * 从JWT令牌中获取所有声明
     * 
     * @param token JWT令牌
     * @return 所有声明
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 检查JWT令牌是否过期
     * 
     * @param token JWT令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * 验证JWT令牌
     * 
     * @param token JWT令牌
     * @param username 用户名
     * @return 是否有效
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }
    
    /**
     * 验证JWT令牌（仅验证格式和过期时间）
     * 
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            // System.out.println("【JWT调试】JwtUtil收到token: " + token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            // System.out.println("【JWT调试】token解析异常: " + e.getMessage());
            return false;
        }
    }
} 