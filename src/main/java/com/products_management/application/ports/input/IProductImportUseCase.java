package com.products_management.application.ports.input;

import com.products_management.infraestructure.input.rest.data.request.ProductImportRequest;
import com.products_management.infraestructure.input.rest.data.response.ProductImportResponse;

/**
 * Puerto de entrada para la funcionalidad de importación de productos.
 * Define el contrato para importar productos desde archivos Excel.
 */
public interface IProductImportUseCase {

    /**
     * Importa productos desde un archivo Excel.
     *
     * @param request solicitud de importación conteniendo archivo y metadatos
     * @return respuesta con resultados de la importación
     */
    ProductImportResponse importProductsFromExcel(ProductImportRequest request);
}