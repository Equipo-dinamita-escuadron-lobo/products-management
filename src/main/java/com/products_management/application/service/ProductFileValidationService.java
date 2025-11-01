package com.products_management.application.service;

import com.products_management.domain.exception.product.ProductFileValidationException;
import com.products_management.domain.utils.ImportConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * Servicio especializado en la validación de archivos para importación de productos.
 * Valida tamaño, extensión y formato del archivo Excel.
 */
@Service
public class ProductFileValidationService {

    /**
     * Valida que el archivo cumpla con los requisitos para importación.
     *
     * @param file archivo a validar
     * @throws ProductFileValidationException si la validación falla
     */
    public void validate(MultipartFile file) {

        // Validar que el archivo no sea null o vacío
        if (file == null || file.isEmpty()) {
            throw new ProductFileValidationException("El archivo no puede estar vacío");
        }

        // Validar tamaño del archivo
        if (file.getSize() > ImportConstants.MAX_FILE_SIZE) {
            throw new ProductFileValidationException(
                String.format("El archivo excede el tamaño máximo permitido de %d MB",
                    ImportConstants.MAX_FILE_SIZE / (1024 * 1024)));
        }

        // Validar extensión del archivo
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new ProductFileValidationException("El archivo no tiene nombre");
        }

        String extension = getFileExtension(fileName);
        if (!isValidExtension(extension)) {
            throw new ProductFileValidationException(
                String.format("Extensión de archivo no soportada: %s. Extensiones permitidas: %s",
                    extension, Arrays.toString(ImportConstants.SUPPORTED_EXTENSIONS)));
        }

    }

    /**
     * Obtiene la extensión del archivo.
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex).toLowerCase();
    }

    /**
     * Verifica si la extensión es válida.
     */
    private boolean isValidExtension(String extension) {
        return Arrays.asList(ImportConstants.SUPPORTED_EXTENSIONS).contains(extension);
    }
}