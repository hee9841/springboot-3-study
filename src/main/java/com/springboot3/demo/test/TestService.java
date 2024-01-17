package com.springboot3.demo.test;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class TestService {

    private final MemberRepository memberRepository;


    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

}
