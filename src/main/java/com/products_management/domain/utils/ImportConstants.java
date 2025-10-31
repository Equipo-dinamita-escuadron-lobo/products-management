package com.products_management.domain.utils;

/**
 * Constantes centralizadas para funcionalidades de importación.
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

    // ===== ENCABEZADOS DE EXCEL =====
    
    /**
     * Encabezados requeridos para importación de terceros.
     * Campos mínimos obligatorios para cualquier tipo de importación.
     */
    public static final String[] REQUIRED_HEADERS = {        
        "Nombre", 
        "Referencia/SKU", 
        "Presentación", 
        "Descripción", 
        "Unidad de Medida", 
        "Categoría",
        "Tipo de Producto"
    };
    
    /**
     * Encabezados opcionales para importación de terceros.
     * Estos campos pueden estar presentes o ausentes en el archivo Excel.
     */
    public static final String[] OPTIONAL_HEADERS = {
        "Código", 
        "Costo",
        "Cantidad",
        "Estado",
    };
    

    // ===== MENSAJES DE ERROR COMUNES =====
    
    /**
     * Mensajes de error estándar para importación.
     */
    public static final class ErrorMessages {
        public static final String SYSTEM_ERROR = "Error del sistema durante la importación";
        
        private ErrorMessages() {}
    }

    // ===== CÓDIGOS DE ERROR =====
    
    /**
     * Códigos de error estandarizados para importación.
     */
    public static final class ErrorCodes {
       
        
        private ErrorCodes() {}
    }

    
    // ===== VALORES POR DEFECTO =====
    
    /**
     * Valores por defecto para configuraciones.
     */
    public static final class Defaults {
        public static final int COLUMN_START_INDEX = 1;
        
        private Defaults() {}
    }
}
