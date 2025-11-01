package com.products_management.domain.utils;

import java.text.Normalizer;

/**
 * Utilidad para normalizar cadenas de texto para validaciones de unicidad.
 * Proporciona métodos para limpiar, normalizar y formatear nombres de manera consistente.
 */
public final class StringNormalizer {

    private StringNormalizer() {
        // Clase utilitaria - constructor privado
    }

    /**
     * Normaliza un nombre de manera consistente para validación y almacenamiento.
     * Elimina tildes/acentos, espacios extra y capitaliza la primera letra.
     * Este formato se usa tanto para comparar como para guardar en la base de datos.
     *
     * @param input el texto a normalizar
     * @return el texto normalizado, o null si el input es null
     */
    public static String normalize(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        String cleaned = removeAccents(input.trim().toLowerCase());
        return capitalizeFirstLetter(cleaned);
    }

    /**
     * Normaliza un código/referencia manteniendo el formato en mayúsculas.
     * Elimina tildes/acentos y espacios extra, pero convierte todo a mayúsculas.
     * Ideal para códigos SKU, referencias, códigos de producto, etc.
     *
     * @param input el código/referencia a normalizar
     * @return el código normalizado en mayúsculas, o null si el input es null
     */
    public static String normalizeCode(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        return removeAccents(input.trim().toUpperCase());
    }

    /**
     * Elimina tildes y acentos de una cadena de texto.
     *
     * @param input el texto del cual eliminar acentos
     * @return el texto sin acentos
     */
    private static String removeAccents(String input) {
        if (input == null) {
            return null;
        }
        
        // Normaliza a forma NFD (descompone caracteres con acentos)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        
        // Elimina los caracteres diacríticos (tildes, acentos, etc.)
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * Normaliza un nombre de header removiendo acentos, convirtiendo a minúsculas y
     * aplicando reglas específicas para comparación.
     */
    public static String normalizeHeaderName(String header) {
        if (header == null) {
            return null;
        }

        // Remover saltos de línea y texto adicional como "(Requerido)", "(Opcional)", etc.
        String cleaned = header.replaceAll("\\s*\\([^)]*\\)\\s*", "") // Remover texto entre paréntesis
                              .replaceAll("\\n.*", "") // Remover todo después del primer salto de línea
                              .trim();

        // Reemplazar caracteres especiales comunes por encoding issues
        cleaned = cleaned.replace('Ý', 'í')  // categoría
                        .replace('¾', 'ó')  // descripción, presentación
                        .replace('Ã', 'í')  // categoría alternativo
                        .replace('³', 'ó'); // descripción alternativo

        // Normalizar acentos y diacríticos
        String normalized = Normalizer.normalize(cleaned, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Convertir a minúsculas
        normalized = normalized.toLowerCase();

        // Aplicar reglas específicas para headers conocidos
        normalized = applyHeaderRules(normalized);

        return normalized.trim();
    }

    /**
     * Aplica reglas específicas para normalizar headers de Excel.
     */
    private static String applyHeaderRules(String header) {
        // Reglas específicas para headers de productos
        switch (header) {
            case "nombre":
            case "name":
                return ImportConstants.NAME_COLUMN;
            case "descripcion":
            case "descripción":
            case "descripci¾n": // Manejar caracteres especiales por encoding
            case "description":
                return ImportConstants.DESCRIPTION_COLUMN;
            case "unidad de medida":
            case "unidad":
            case "uom":
                return ImportConstants.UNIT_MEASURE_COLUMN;
            case "categoria":
            case "categoría":
            case "categorÝa": // Manejar caracteres especiales por encoding
            case "category":
                return ImportConstants.CATEGORY_COLUMN;
            case "tipo de producto":
            case "tipo":
            case "product type":
                return ImportConstants.PRODUCT_TYPE_COLUMN;
            case "referencia/sku": // Manejar el formato de exportación primero (más específico)
            case "referencia":
            case "sku":
            case "reference":
                return ImportConstants.REFERENCE_COLUMN;
            case "presentacion":
            case "presentación":
            case "presentaci¾n": // Manejar caracteres especiales por encoding
            case "presentation":
                return ImportConstants.PRESENTATION_COLUMN;
            case "cantidad":
            case "quantity":
                return ImportConstants.QUANTITY_COLUMN;
            case "costo":
            case "cost":
            case "price":
                return ImportConstants.COST_COLUMN;
            case "codigo":
            case "code":
                return "Código"; // Campo opcional
            case "estado":
            case "state":
            case "status":
                return "Estado"; // Campo opcional
            default:
                return header;
        }
    }

    /**
     * Capitaliza la primera letra de una cadena.
     *
     * @param input el texto a capitalizar
     * @return el texto con la primera letra en mayúscula
     */
    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        if (input.length() == 1) {
            return input.toUpperCase();
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
