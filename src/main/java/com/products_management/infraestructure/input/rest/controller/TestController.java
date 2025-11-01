package com.products_management.infraestructure.input.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * Controlador de prueba para verificar el funcionamiento del servicio.
 */
@RestController
@RequestMapping("/api/categories/test")
public class TestController {


    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
