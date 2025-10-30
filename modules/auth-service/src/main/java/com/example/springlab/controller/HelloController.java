package com.example.springlab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/public")
    public String pub() { return "public ok"; }

    @GetMapping("/user")
    public String user() { return "user ok"; }

    @GetMapping("/admin")
    public String admin() { return "admin ok"; }
}
