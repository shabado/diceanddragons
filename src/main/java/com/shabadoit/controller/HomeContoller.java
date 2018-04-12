package com.shabadoit.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeContoller {

    @RequestMapping("/")
    public String home() {
        return "Nothing to see here yet";
    }
}
