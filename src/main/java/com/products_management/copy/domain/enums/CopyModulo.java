package com.products_management.copy.domain.enums;

/**
 * Módulos participantes reconocidos por el bounded context de copia.
 * El valor es el nombre canónico utilizado en el campo "tabla" de las equivalencias.
 */
public enum CopyModulo {

    /** Catálogo de cuentas. */
    CATALOGUE,

    /** Gestión de productos (este servicio). */
    PRODUCTS,

    /** Gestión de terceros. */
    THIRDS,

    /** Gestión de empresas. */
    ENTERPRISES
}
