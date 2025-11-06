package com.products_management.application.ports.input;

import org.springframework.core.io.Resource;

/**
 * @brief Puerto de entrada para exportación de productos a Excel
 *
 * Define contrato para generación de archivos Excel con productos:
 * - Plantillas con validaciones (listas desplegables)
 * - Exportación de datos existentes con filtros por estado
 */
public interface IProductExportUseCase {

    /**
     * @brief Exporta plantilla de productos con validaciones Excel
     * @param entId ID de la entidad
     * @return Resource que contiene la plantilla Excel con validaciones
     */
    Resource exportProductTemplateWithValidations(String entId);

    /**
     * @brief Exporta productos existentes con validaciones Excel
     * @param entId ID de la entidad
     * @param status estado de los productos (true=activos, false=inactivos, null=todos)
     * @return Resource que contiene el archivo Excel con datos y validaciones
     */
    Resource exportProductsWithValidations(String entId, Boolean status);

}
