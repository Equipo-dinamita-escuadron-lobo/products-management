package com.products_management.infraestructure.input.validation;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interfaz para validadores de archivos.
 * Define el contrato para validar diferentes tipos de archivos.
 */
public interface FileValidator {

    /**
     * Valida un archivo según las reglas específicas del tipo.
     *
     * @param file archivo a validar
     * @throws Exception si la validación falla
     */
    void validate(MultipartFile file);

    /**
     * Obtiene los tipos MIME soportados por este validador.
     *
     * @return array de tipos MIME soportados
     */
    String[] getSupportedMimeTypes();

    /**
     * Obtiene las extensiones de archivo soportadas por este validador.
     *
     * @return array de extensiones soportadas
     */
    String[] getSupportedExtensions();
}