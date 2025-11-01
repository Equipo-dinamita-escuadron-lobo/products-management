package com.products_management.infraestructure.input.rest.dto.response;

import com.products_management.domain.enums.ImportStatus;
import com.products_management.domain.model.ImportErrorDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para respuesta de importación de productos.
 * Contiene estadísticas, estado y errores de la importación.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductImportResponse {

    /**
     * Identificador de la empresa.
     */
    private String entId;

    /**
     * Nombre del archivo procesado.
     */
    private String fileName;

    /**
     * Estado final de la importación.
     */
    private ImportStatus status;

    /**
     * Total de registros encontrados en el archivo.
     */
    private int totalRecords;

    /**
     * Número de productos importados exitosamente.
     */
    private int successfulImports;

    /**
     * Número de productos que fallaron durante la importación.
     */
    private int failedImports;

    /**
     * Número de productos duplicados que fueron omitidos.
     */
    private int duplicatesSkipped;

    /**
     * Lista detallada de errores encontrados durante la importación.
     * Solo se incluye si hay errores.
     */
    private List<ImportErrorDetail> errors;
}