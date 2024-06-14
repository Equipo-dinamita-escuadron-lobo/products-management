package com.products_management.infraestructure.input.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de prueba para verificar el funcionamiento del servicio.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/categories/test")
public class TestController {

    /**
     * Método para verificar la disponibilidad del servicio.
     * @return Cadena "pong" como respuesta exitosa.
     */
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
