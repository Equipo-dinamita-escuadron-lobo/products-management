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
 * @brief DTO de respuesta para operaciones de importación de productos
 *
 * Proporciona estadísticas completas del proceso de importación masiva,
 * incluyendo métricas de éxito, fallos y errores detallados cuando ocurren.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductImportResponse {

    private String entId;
    private String fileName;
    private ImportStatus status;
    private int totalRecords;
    private int successfulImports;
    private int failedImports;
    private int duplicatesSkipped;
    private List<ImportErrorDetail> errors;
}