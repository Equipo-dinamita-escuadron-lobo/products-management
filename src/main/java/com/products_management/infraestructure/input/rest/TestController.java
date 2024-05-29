package com.products_management.infraestructure.input.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/categories/test")
public class TestController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
