package com.springboot3.demo.user.service;

import com.springboot3.demo.user.domain.User;
import com.springboot3.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
 */
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자 이름을(email)으로 사용자의 정보 가져오는 메서도으
     * @param email the username identifying the user whose data is required.
     * @return User
     */
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(email));
    }
}
