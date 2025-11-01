package com.products_management.domain.utils;

/**
 * Constantes centralizadas para funcionalidades de importación de productos.
 * Centraliza todos los valores constantes utilizados en el proceso de importación
 * para facilitar mantenimiento y reutilización.
 */
public final class ImportConstants {

    private ImportConstants() {
        throw new UnsupportedOperationException("ImportConstants es una clase de utilidad y no debe ser instanciada");
    }

    // ===== CONFIGURACIÓN DE ARCHIVOS =====

    /**
     * Extensiones de archivo soportadas para importación.
     */
    public static final String[] SUPPORTED_EXTENSIONS = {".xlsx", ".xls"};

    /**
     * Tamaño máximo de archivo en bytes (5 MB).
     */
    public static final long MAX_FILE_SIZE = 5242880L; // 5 * 1024 * 1024

    // ===== NOMBRES DE COLUMNAS =====

    public static final String NAME_COLUMN = "Nombre";
    public static final String DESCRIPTION_COLUMN = "Descripción";
    public static final String UNIT_MEASURE_COLUMN = "Unidad de Medida";
    public static final String CATEGORY_COLUMN = "Categoría";
    public static final String PRODUCT_TYPE_COLUMN = "Tipo de Producto";
    public static final String REFERENCE_COLUMN = "Referencia";
    public static final String PRESENTATION_COLUMN = "Presentación";
    public static final String QUANTITY_COLUMN = "Cantidad";
    public static final String COST_COLUMN = "Costo";

    // ===== ENCABEZADOS DE EXCEL =====

    /**
     * Encabezados requeridos para importación de productos.
     * Todos son obligatorios excepto costo y cantidad.
     */
    public static final String[] REQUIRED_HEADERS = {
        NAME_COLUMN,
        DESCRIPTION_COLUMN,
        UNIT_MEASURE_COLUMN,
        CATEGORY_COLUMN,
        PRODUCT_TYPE_COLUMN,
        REFERENCE_COLUMN,
        PRESENTATION_COLUMN
    };

    /**
     * Encabezados opcionales para importación de productos.
     * Estos campos pueden estar presentes o ausentes, y pueden estar vacíos.
     */
    public static final String[] OPTIONAL_HEADERS = {
        QUANTITY_COLUMN,
        COST_COLUMN
    };

    // ===== VALORES BOOLEANOS ACEPTADOS =====

    /**
     * No aplicable para productos (no hay campos booleanos en la importación).
     */

    // ===== CÓDIGOS DE ERROR =====

    /**
     * Códigos de error estandarizados para importación.
     */
    public static final class ErrorCodes {
        public static final String REQUIRED_FIELD_MISSING = "REQUIRED_FIELD_MISSING";
        public static final String INVALID_FORMAT = "INVALID_FORMAT";
        public static final String INVALID_NUMBER = "INVALID_NUMBER";
        public static final String INVALID_REFERENCE = "INVALID_REFERENCE";
        public static final String DUPLICATE_PRODUCT = "DUPLICATE_PRODUCT";
        public static final String BUSINESS_RULE_VIOLATION = "BUSINESS_RULE_VIOLATION";
        public static final String SYSTEM_ERROR = "SYSTEM_ERROR";

        private ErrorCodes() {}
    }

    // ===== MENSAJES DE ERROR COMUNES =====

    /**
     * Mensajes de error estándar para importación.
     */
    public static final class ErrorMessages {
        public static final String SYSTEM_ERROR = "Error del sistema durante la importación";
        public static final String EMPTY_FILE = "El archivo no contiene datos para importar";
        public static final String INVALID_HEADERS = "El archivo no contiene los encabezados requeridos";

        private ErrorMessages() {}
    }

    // ===== VALORES POR DEFECTO =====

    /**
     * Valores por defecto para configuraciones.
     */
    public static final class Defaults {
        public static final int COLUMN_START_INDEX = 1;
        public static final int BATCH_SIZE = 500;
        public static final boolean SKIP_DUPLICATES = true;
        public static final boolean CONTINUE_ON_ERROR = true;

        private Defaults() {}
    }

    // ===== VALIDACIONES DE DATOS =====

    /**
     * Validaciones específicas para campos numéricos.
     */
    public static final class Validations {
        public static final long MAX_QUANTITY = Long.MAX_VALUE;
        public static final double MAX_COST = Double.MAX_VALUE;
        public static final int MAX_REFERENCE_LENGTH = 255;
        public static final int MAX_PRESENTATION_LENGTH = 255;

        private Validations() {}
    }
}
