package com.products_management.application.service;

import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.application.ports.output.IProductTypePersistencePort;
import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.exception.ErrorCode;
import com.products_management.domain.exception.product.ExcelValidationException;
import com.products_management.domain.model.Category;
import com.products_management.domain.model.ProductType;
import com.products_management.domain.model.UnitOfMeasure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio centralizado para validaciones de Excel.
 * Maneja tanto la obtención de datos como la aplicación de validaciones.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductExcelValidationService {

    private final ICategoryPersistencePort categoryPersistencePort;
    private final IProductTypePersistencePort productTypePersistencePort;
    private final IUnitOfMeasurePersistencePort unitOfMeasurePersistencePort;

    // Constantes para mensajes
    private static final String VALIDATION_ERROR_TITLE = "Error de Validación";
    private static final String ENTER_PROMPT_PREFIX = "Ingrese ";

    // ========== MÉTODOS DE OBTENCIÓN DE DATOS ==========

    /**
     * Obtiene todas las categorías activas para una entidad.
     */
    public List<String> getCategoryOptions(String entId) {
        // Usar paginación con tamaño grande para obtener todas las categorías activas
        var page = categoryPersistencePort.getActiveCategoriesBy(entId, 0, 1000, "name", "asc");
        return page.getContent().stream()
                .map(Category::getName)
                .toList();
    }

    /**
     * Obtiene todos los tipos de producto activos para una entidad.
     */
    public List<String> getProductTypeOptions(String entId) {
        // Usar paginación con tamaño grande para obtener todos los tipos activos
        var page = productTypePersistencePort.findActivatedByEnterpriseId(entId, 0, 1000);
        return page.getContent().stream()
                .map(ProductType::getName)
                .toList();
    }

    /**
     * Obtiene todas las unidades de medida activas para una entidad por nombre completo.
     */
    public List<String> getUnitOfMeasureOptions(String entId) {
        // Usar paginación con tamaño grande para obtener todas las unidades activas
        var page = unitOfMeasurePersistencePort.getActiveUnitOfMeasuresBy(entId, 0, 1000, "name", "asc");
        return page.getContent().stream()
                .map(UnitOfMeasure::getName)
                .toList();
    }

    /**
     * Obtiene todas las opciones de estado (activo/inactivo).
     */
    public List<String> getStatusOptions() {
        return List.of("ACTIVO", "INACTIVO");
    }

    // ========== MÉTODOS DE APLICACIÓN DE VALIDACIONES ==========

    /**
     * Aplica todas las validaciones de datos a una hoja de Excel para productos.
     *
     * @param sheet hoja de Excel
     * @param entId ID de la empresa
     * @param startRow fila inicial
     * @param endRow fila final
     */
    public void applyProductValidations(Sheet sheet, String entId, int startRow, int endRow) throws ExcelValidationException {
        // Columna 0: Código (validación de texto personalizado)
        applyCodeValidation(sheet, 0, startRow, endRow);

        // Columna 1: Nombre (validación básica de texto)
        applyTextValidation(sheet, 1, startRow, endRow, "Nombre del producto");

        // Columna 2: Referencia/SKU (validación básica de texto)
        applyTextValidation(sheet, 2, startRow, endRow, "Referencia/SKU del producto");

        // Columna 3: Presentación (validación básica de texto)
        applyTextValidation(sheet, 3, startRow, endRow, "Presentación del producto");

        // Columna 4: Descripción (validación básica de texto)
        applyTextValidation(sheet, 4, startRow, endRow, "Descripción del producto");

        // Columna 5: Costo (validación numérica decimal)
        applyDecimalValidation(sheet, 5, startRow, endRow, "Costo del producto");

        // Columna 6: Cantidad (validación numérica entera)
        applyIntegerValidation(sheet, 6, startRow, endRow, "Cantidad del producto");

        // Columna 7: Unidad de Medida
        applyDropdownValidation(sheet, 7, startRow, endRow,
                getUnitOfMeasureOptions(entId),
                "Seleccione una unidad de medida válida");

        // Columna 8: Categoría
        applyDropdownValidation(sheet, 8, startRow, endRow,
                getCategoryOptions(entId),
                "Seleccione una categoría válida");

        // Columna 9: Tipo de Producto
        applyDropdownValidation(sheet, 9, startRow, endRow,
                getProductTypeOptions(entId),
                "Seleccione un tipo de producto válido");

        // Columna 10: Estado
        applyDropdownValidation(sheet, 10, startRow, endRow,
                getStatusOptions(),
                "Seleccione ACTIVO o INACTIVO");
    }

    /**
     * Aplica validación personalizada para el código de producto.
     */
    public void applyCodeValidation(Sheet sheet, int columnIndex, int startRow, int endRow) throws ExcelValidationException {
        try {
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(xssfSheet);

            CellRangeAddressList addressList = new CellRangeAddressList(startRow, endRow, columnIndex, columnIndex);

            // Validación personalizada: texto no vacío
            // Convertir índice de columna a letra de columna (0=A, 1=B, etc.)
            String columnLetter = getColumnLetter(columnIndex);
            String formula = "LEN(TRIM(" + columnLetter + (startRow + 1) + ")) > 0";

            DataValidationConstraint constraint = validationHelper.createCustomConstraint(formula);

            DataValidation validation = validationHelper.createValidation(constraint, addressList);

            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox(VALIDATION_ERROR_TITLE,
                    "El código del producto es obligatorio");

            validation.setShowPromptBox(true);
            validation.createPromptBox("Código de Producto",
                    "Ingrese un código único para el producto");

            sheet.addValidationData(validation);

        } catch (Exception e) {
            throw new ExcelValidationException(ErrorCode.EXCEL_VALIDATION_ERROR, "Error al aplicar validación de código", e);
        }
    }

    /**
     * Aplica validación básica de texto.
     */
    public void applyTextValidation(Sheet sheet, int columnIndex, int startRow, int endRow, String fieldName) throws ExcelValidationException {
        try {
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(xssfSheet);

            CellRangeAddressList addressList = new CellRangeAddressList(startRow, endRow, columnIndex, columnIndex);

            // Validación personalizada: texto no vacío
            // Convertir índice de columna a letra de columna (0=A, 1=B, etc.)
            String columnLetter = getColumnLetter(columnIndex);
            String formula = "LEN(TRIM(" + columnLetter + (startRow + 1) + ")) > 0";

            DataValidationConstraint constraint = validationHelper.createCustomConstraint(formula);

            DataValidation validation = validationHelper.createValidation(constraint, addressList);

            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox(VALIDATION_ERROR_TITLE,
                    "El campo " + fieldName.toLowerCase() + " es obligatorio");

            validation.setShowPromptBox(true);
            validation.createPromptBox(fieldName,
                    ENTER_PROMPT_PREFIX + fieldName.toLowerCase());

            sheet.addValidationData(validation);

        } catch (Exception e) {
            throw new ExcelValidationException(ErrorCode.EXCEL_VALIDATION_ERROR, "Error al aplicar validación de texto en columna " + columnIndex, e);
        }
    }

    /**
     * Aplica validación de número decimal.
     */
    public void applyDecimalValidation(Sheet sheet, int columnIndex, int startRow, int endRow, String fieldName) throws ExcelValidationException {
        try {
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(xssfSheet);

            CellRangeAddressList addressList = new CellRangeAddressList(startRow, endRow, columnIndex, columnIndex);

            // Validación numérica decimal (mayor o igual a 0)
            DataValidationConstraint constraint = validationHelper.createDecimalConstraint(
                    DataValidationConstraint.OperatorType.GREATER_OR_EQUAL,
                    "0",
                    "999999999.99");

            DataValidation validation = validationHelper.createValidation(constraint, addressList);

            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox(VALIDATION_ERROR_TITLE,
                    "El " + fieldName.toLowerCase() + " debe ser un número mayor o igual a 0");

            validation.setShowPromptBox(true);
            validation.createPromptBox(fieldName,
                    ENTER_PROMPT_PREFIX + fieldName.toLowerCase() + " (ejemplo: 150.50)");

            // Permitir celdas vacías (campo opcional)
            validation.setEmptyCellAllowed(true);

            sheet.addValidationData(validation);

        } catch (Exception e) {
            throw new ExcelValidationException(ErrorCode.EXCEL_VALIDATION_ERROR, "Error al aplicar validación decimal en columna " + columnIndex, e);
        }
    }

    /**
     * Aplica validación de número entero.
     */
    public void applyIntegerValidation(Sheet sheet, int columnIndex, int startRow, int endRow, String fieldName) throws ExcelValidationException {
        try {
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(xssfSheet);

            CellRangeAddressList addressList = new CellRangeAddressList(startRow, endRow, columnIndex, columnIndex);

            // Validación numérica entera (mayor o igual a 0)
            DataValidationConstraint constraint = validationHelper.createIntegerConstraint(
                    DataValidationConstraint.OperatorType.GREATER_OR_EQUAL,
                    "0",
                    "999999999");

            DataValidation validation = validationHelper.createValidation(constraint, addressList);

            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox(VALIDATION_ERROR_TITLE,
                    "La " + fieldName.toLowerCase() + " debe ser un número entero mayor o igual a 0");

            validation.setShowPromptBox(true);
            validation.createPromptBox(fieldName,
                    ENTER_PROMPT_PREFIX + fieldName.toLowerCase() + " (ejemplo: 100)");

            // Permitir celdas vacías (campo opcional)
            validation.setEmptyCellAllowed(true);

            sheet.addValidationData(validation);

        } catch (Exception e) {
            throw new ExcelValidationException(ErrorCode.EXCEL_VALIDATION_ERROR, "Error al aplicar validación entera en columna " + columnIndex, e);
        }
    }

    /**
     * Aplica validación de lista desplegable a una columna específica.
     */
    public void applyDropdownValidation(Sheet sheet, int columnIndex, int startRow, int endRow,
            List<String> options, String errorMessage) {
        if (options == null || options.isEmpty()) {
            return;
        }

        try {
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            XSSFDataValidationHelper validationHelper = new XSSFDataValidationHelper(xssfSheet);

            // Crear el rango de celdas donde aplicar la validación
            CellRangeAddressList addressList = new CellRangeAddressList(startRow, endRow, columnIndex, columnIndex);

            // Crear la lista de opciones
            String[] optionsArray = options.toArray(new String[0]);
            DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(optionsArray);

            // Crear la validación
            DataValidation validation = validationHelper.createValidation(constraint, addressList);

            // Configurar propiedades de la validación
            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox(VALIDATION_ERROR_TITLE, errorMessage);

            // Mostrar lista desplegable
            validation.setShowPromptBox(true);
            validation.createPromptBox("Selección", "Seleccione una opción de la lista");

            // Aplicar la validación a la hoja
            sheet.addValidationData(validation);

        } catch (Exception e) {
            throw new ExcelValidationException(ErrorCode.EXCEL_VALIDATION_ERROR,
                    "Error al aplicar validación en columna " + columnIndex, e);
        }
    }

    /**
     * Convierte un índice de columna (0-based) a la letra de columna de Excel (A, B, C, ..., Z, AA, AB, etc.).
     */
    private String getColumnLetter(int columnIndex) {
        StringBuilder columnLetter = new StringBuilder();
        int temp = columnIndex;

        while (temp >= 0) {
            columnLetter.insert(0, (char) ('A' + (temp % 26)));
            temp = (temp / 26) - 1;
        }

        return columnLetter.toString();
    }
}
