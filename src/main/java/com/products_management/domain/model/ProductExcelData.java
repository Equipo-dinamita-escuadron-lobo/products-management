package com.products_management.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo que representa los datos de un producto extraídos del archivo Excel.
 * Contiene los campos que se pueden importar desde Excel.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductExcelData {

    /**
     * Número de fila en el Excel (1-indexed).
     */
    private int rowNumber;

    /**
     * Identificador de la empresa.
     */
    private String enterpriseId;

    /**
     * Nombre del producto.
     */
    private String name;

    /**
     * Descripción del producto.
     */
    private String description;

    /**
     * Cantidad del producto (opcional).
     */
    private Integer quantity;

    /**
     * ID de la unidad de medida.
     */
    private Long unitOfMeasureId;

    /**
     * ID de la categoría.
     */
    private Long categoryId;

    /**
     * ID del tipo de producto.
     */
    private Long productTypeId;

    /**
     * Costo del producto (opcional).
     */
    private Double cost;

    /**
     * Referencia del producto.
     */
    private String reference;

    /**
     * Presentación del producto.
     */
    private String presentation;

    /**
     * Nombres temporales para lookup (se convierten a IDs después).
     */
    private String unitOfMeasureName;
    private String categoryName;
    private String productTypeName;
}