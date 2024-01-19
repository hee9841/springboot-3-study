package com.springboot3.demo.config;


import com.springboot3.demo.user.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    //스프링 시큐리티 기능 비활성화

    /**
     * 스프링 시큐리티의 모든 기능을 사용하지 않게 설정하는 코드
     */
    @Bean
    public WebSecurityCustomizer configure() {
        //static 하위 경로에 있는 리소스, h2 데이터를 확인하는 h2 console 하위 url을 대상으로 ignore
        return (web) -> web.ignoring().requestMatchers(PathRequest.toH2Console()) //3.X 이후 변경
            .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests()
            .requestMatchers("/login", "/signup", "/user").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/articles")
            .and()
            .logout()
            .logoutSuccessUrl("/login")
            .invalidateHttpSession(true)    //logout 완료 후 세션 전체을 삭제할지 여부를 설정
            .and()
            .csrf().disable()   //csrf 공격을 방지하기 위해서 활성화하는게 좋지만 실습을 편리하게 하기 위해 비활성화
            .build();
    }

    /**
     * 인증 관리자 관련 설정
     * 사용자 정보를 가져올 서비스를 재정의하거나
     * 인증 방법(LDAP, JDBC 기반 인증 등을 재설정할 떄)
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
        BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService UserDetailService)
        throws Exception {
        
        return http
            .getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userService)
            .passwordEncoder(bCryptPasswordEncoder)
            .and()
            .build();
        
        //userDetailsService(userService) 
        // ->  사용자 정보를 가져올 서비스를 설정. 이때 설정하는 서비스 클래스는 반드시 userDetailsService 상속
        //passwordEncoder() -> 비밀번호 암호화하기 위한 인코더 설정
    }


    /**
     * 패스워드 인코더로 사용할 빈 등록
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
