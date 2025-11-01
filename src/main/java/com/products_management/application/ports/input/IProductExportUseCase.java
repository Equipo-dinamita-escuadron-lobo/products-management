package com.products_management.application.ports.input;

import org.springframework.core.io.Resource;

/**
 * Caso de uso para exportar productos en formato Excel.
 */
public interface IProductExportUseCase {

    /**
     * Exporta una plantilla de productos con validaciones de datos (listas desplegables).
     *
     * @param entId ID de la entidad
     * @return Resource que contiene la plantilla Excel con validaciones
     */
    Resource exportProductTemplateWithValidations(String entId);

    /**
     * Exporta productos existentes con validaciones de datos (listas desplegables).
     * Combina los datos reales con las validaciones de la plantilla.
     *
     * @param entId ID de la entidad
     * @param status Estado de los productos (true=activos, false=inactivos, null=todos)
     * @return Resource que contiene el archivo Excel con datos y validaciones
     */
    Resource exportProductsWithValidations(String entId, Boolean status);

}
