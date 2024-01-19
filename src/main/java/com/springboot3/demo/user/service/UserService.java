package com.springboot3.demo.user.service;

import com.springboot3.demo.user.domain.User;
import com.springboot3.demo.user.dto.AddUserRequest;
import com.springboot3.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public Long save(AddUserRequest userRequest) {
        return userRepository.save(User.builder()
            .email(userRequest.getEmail())
            .password(encoder.encode(userRequest.getPassword()))
            .build()).getId();
    }

}
