package com.products_management.infraestructure.input.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Controlador de prueba para verificar el funcionamiento del servicio.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/categories/test")
public class TestController {

    @Operation(summary = "Verificar la disponibilidad del servicio", description = "Este endpoint verifica que el servicio esté activo y responde con un mensaje 'pong'.", responses = {
            @ApiResponse(responseCode = "200", description = "Servicio disponible", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
