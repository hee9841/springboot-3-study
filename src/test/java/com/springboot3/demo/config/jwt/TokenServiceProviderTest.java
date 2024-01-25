package com.springboot3.demo.config.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.springboot3.demo.user.domain.User;
import com.springboot3.demo.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;


@SpringBootTest
class TokenServiceProviderTest {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProperties jwtproperties;


    private Key key;


    @BeforeEach
    void beforeEach() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtproperties.getSecret());
        key = Keys.hmacShaKeyFor(keyBytes);

//        userRepository.deleteAll();
    }


    @DisplayName("getrateToken() : 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        //given
        User testUser = userRepository.save(User.builder()
            .email("user@email.com")
            .password("test")
            .build());

        //when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        //then
        Long userId = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToke() : 만료된 토큰일 때 유효성 검금증에 실패")
    @Test
    void validToken_invalidToken() {
        //given
        String token = JwtFactory.builder()
            .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
            .build().createToken(jwtproperties);

        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("validToken() : 유요한 토큰일 때 유효성 검증에 성공함")
    @Test
    void validToke_validToken() {
        //given
        String token = JwtFactory.withDefaultValues()
            .createToken(jwtproperties);

        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication() : 토큰 기반으로 인증정보 정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        //given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
            .subject(userEmail)
            .build()
            .createToken(jwtproperties);

        //when
        Authentication authentication = tokenProvider.getAuthentication(token);

        //then
        String userName = ((UserDetails) authentication.getPrincipal()).getUsername();
        assertThat(userName).isEqualTo(userEmail);
    }

    @DisplayName("getUserId() : 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUSerId() {
        //given
        Long userId = 1L;
        String token = JwtFactory.builder()
            .claims(Map.of("id", userId))
            .build()
            .createToken(jwtproperties);

        //when
        Long userIdByToken = tokenProvider.getUserId(token);

        //then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
