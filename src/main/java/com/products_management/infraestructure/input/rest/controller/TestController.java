package com.products_management.infraestructure.input.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @brief Controlador de pruebas para verificación de servicios
 *
 * Proporciona endpoints básicos de prueba para validar funcionamiento
 * del sistema y conectividad de la API REST.
 */
@RestController
@RequestMapping("/api/categories/test")
public class TestController {


    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
