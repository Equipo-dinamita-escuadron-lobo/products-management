package com.products_management.application.ports.input;

import com.products_management.infraestructure.input.rest.dto.request.ProductImportRequest;
import com.products_management.infraestructure.input.rest.dto.response.ProductImportResponse;

/**
 * @brief Puerto de entrada para importación masiva de productos desde Excel
 *
 * Define contrato para procesamiento de archivos Excel con datos de productos,
 * incluyendo validación, transformación y persistencia masiva de productos.
 */
public interface IProductImportUseCase {

    /**
     * @brief Importa productos desde archivo Excel
     * @param request solicitud de importación conteniendo archivo y metadatos
     * @return respuesta con resultados de la importación
     */
    ProductImportResponse importProductsFromExcel(ProductImportRequest request);
}