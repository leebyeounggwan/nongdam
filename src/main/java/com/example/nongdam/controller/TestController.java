package com.example.nongdam.controller;

import com.example.nongdam.entity.Hello;
import com.example.nongdam.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestRepository testRepository;

    @GetMapping("/")
    public String hello () {

        Optional<Hello> hello = testRepository.findById(1L);
        Hello hello1 = hello.get();

        return hello1.getText();
    }
}
