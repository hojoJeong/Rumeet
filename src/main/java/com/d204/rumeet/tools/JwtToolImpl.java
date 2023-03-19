package com.d204.rumeet.tools;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
public class JwtToolImpl implements JwtTool{
    private final String SECRET_KEY = "wjddmssecretKeywjddms";
    public String createAccessToken(int id) {
        Claims claims = Jwts.claims();
        claims.put("id", id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact();
    }

    public String createRefreshToken(int id) {
        Claims claims = Jwts.claims();
        claims.put("id", id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48))
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact();
    }

    @Override
    public boolean checkExpire(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(this.generateKey())
                .parseClaimsJws(token)
                .getBody();
        long expTimestamp = Long.parseLong(String.valueOf(claims.get("exp")));
       
        if(System.currentTimeMillis() < expTimestamp * 1000) return true;

        return false;
    }

    public byte[] generateKey() {
        byte[] key = null;
        try {
            // charset 설정 안하면 사용자 플랫폼의 기본 인코딩 설정으로 인코딩 됨.
            key = SECRET_KEY.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return key;
    }
    public boolean validateToken(String token) {
        try {
            Jwts
                .parser()
                .setSigningKey(this.generateKey())
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            //DEBUG 가능
        }
        return false;
    }
}
