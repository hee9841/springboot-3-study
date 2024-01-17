package com.springboot3.demo.test;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {


    private final TestService testService;


    @GetMapping("/test")
    public List<Member> getAllMembers() {
        return testService.getAllMembers();
    }

}
