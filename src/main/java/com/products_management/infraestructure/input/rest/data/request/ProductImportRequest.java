package com.products_management.infraestructure.input.rest.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para solicitud de importación de productos.
 * Contiene el archivo Excel y metadatos necesarios para la importación.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImportRequest {

    /**
     * Identificador de la empresa a la que pertenecen los productos.
     */
    @NotBlank(message = "El ID de empresa es requerido")
    private String entId;

    /**
     * Archivo Excel con los datos de los productos.
     */
    @NotNull(message = "El archivo Excel es requerido")
    private MultipartFile excelFile;

    /**
     * Nombre original del archivo (generalmente tomado del MultipartFile).
     */
    private String fileName;

    /**
     * Constructor de conveniencia para crear request desde parámetros del controlador.
     */
    public static ProductImportRequest from(String entId, MultipartFile excelFile) {
        return ProductImportRequest.builder()
                .entId(entId)
                .excelFile(excelFile)
                .fileName(excelFile != null ? excelFile.getOriginalFilename() : null)
                .build();
    }
}