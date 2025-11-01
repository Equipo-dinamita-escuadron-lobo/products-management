package com.products_management.domain.enums;

/**
 * Enum que representa los tipos de errores que pueden ocurrir durante la importación.
 */
public enum ImportErrorType {
    FORMAT_ERROR,
    VALIDATION_ERROR,
    BUSINESS_RULE_VIOLATION,
    DUPLICATE_ERROR,
    SYSTEM_ERROR,
    MISSING_DATA
}