package com.products_management.infraestructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @brief Propiedades de configuración para carga de archivos
 *
 * Externaliza configuración de límites y restricciones de archivos al application.yml,
 * permitiendo personalizar validaciones de tamaño, extensiones y tipos MIME por tipo de archivo.
 */
@Data
@ConfigurationProperties(prefix = "file-upload")
public class FileUploadProperties {
    
    private Long maxSize;
    private Map<String, List<String>> allowedExtensions;
    private Map<String, List<String>> allowedMimeTypes;
}
