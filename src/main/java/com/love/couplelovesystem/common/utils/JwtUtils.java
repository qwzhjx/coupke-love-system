package com.love.couplelovesystem.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JWT 工具类
 */
public class JwtUtils {

    // 生产环境应放到配置文件
    private static final String SECRET = "coupleLoveSystemSecretKey2026";
    private static final long EXPIRE = 86400000L; // 24小时

    /**
     * 生成 Token
     */
    public static String generateToken(Integer adminId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("adminId", adminId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 解析 Token
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证 Token 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 Token 获取用户名
     */
    public static String getUsername(String token) {
        return parseToken(token).getSubject();
    }
}
