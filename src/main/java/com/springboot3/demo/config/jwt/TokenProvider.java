package com.springboot3.demo.config.jwt;

import com.springboot3.demo.user.domain.User;
import com.springboot3.demo.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private Key key;


    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);


    }


    //todo 리팩필요
//    private Key getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToke(new Date(now.getTime() + expiredAt.toMillis()), user);
    }


    //1. jwt 토큰 생성 메서드
    private String makeToke(Date expiry, User user) {
        Date now = new Date();

//        Jwts.builder()
//            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
//            .setIssuer("test")
//            .setIssuedAt(now)
//            .setExpiration(expiry)
//            .setSubject(user.getEmail());

        String restun = Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer(jwtProperties.getIssuer())
            .setIssuedAt(now)
            .setExpiration(expiry)
            .setSubject(user.getEmail())
            .claim("id", user.getId())
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return restun;

    }


    //2. jwt 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
//        Old code:
//        Jwts.parser()
//            .requireAudience("string")
//            .parse(jwtString)

//        New code:
//        Jwts.parserBuilder()
//            .requireAudience("string")
//            .build()

        //Jwts.parser().verifyWith(key).build().parseSignedClaims(compactJws);

        try {
            //토큰 복호화
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            // 유요한 토큰이면
            return true;
        } catch (Exception e) {
            //유요하지 않으면
            return false;
        }
    }

    //3. 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        //토큰값으로 복호화후 ㅋ
        Claims claims = getClaims(token);


        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
            new SimpleGrantedAuthority("ROLE_USER"));

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
            claims.getSubject(), "",
            authorities);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            user, token, authorities);

        return authenticationToken;
    }


    //4. 토큰 기반으로 유저 id를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token);

        Long id = claims.get("id", Long.class);

        return id;
    }

    //복호화 후 클레임 가져옴
    // 클레임(claim) : 정보(payload)의 한 ‘조각’, key-value 한쌍으로 잉루어짐
    private Claims getClaims(String token) {
        Claims body = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return body;

    }


}
