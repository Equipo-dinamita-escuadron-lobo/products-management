package com.products_management.infraestructure.input.validation;

import com.products_management.domain.exception.product.ProductFileSizeExceededException;
import com.products_management.domain.exception.product.ProductFileValidationException;
import com.products_management.infraestructure.config.FileUploadProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Validador específico para archivos Excel (.xlsx, .xls).
 * Implementa validaciones de tamaño, extensión y tipo MIME para Excel.
 */
@Component
@RequiredArgsConstructor
public class ExcelFileValidator implements FileValidator {

    private final FileUploadProperties fileProperties;

    @Override
    public void validate(MultipartFile file) {
        validateNotNull(file);
        validateNotEmpty(file);
        validateSize(file);
        validateExtension(file);
    }

    private void validateNotNull(MultipartFile file) {
        if (file == null) {
            throw ProductFileValidationException.forNullFile();
        }
    }

    private void validateNotEmpty(MultipartFile file) {
        if (file.isEmpty() || file.getSize() == 0) {
            throw ProductFileValidationException.forEmptyFile(file.getOriginalFilename());
        }
    }

    private void validateSize(MultipartFile file) {
        if (file.getSize() > fileProperties.getMaxSize()) {
            throw new ProductFileSizeExceededException(fileProperties.getMaxSize());
        }
    }

    private void validateExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw ProductFileValidationException.forInvalidExtension("archivo sin nombre", getSupportedExtensions());
        }

        String lowerFilename = filename.toLowerCase();
        boolean validExtension = false;

        for (String ext : getSupportedExtensions()) {
            if (lowerFilename.endsWith(ext)) {
                validExtension = true;
                break;
            }
        }

        if (!validExtension) {
            throw ProductFileValidationException.forInvalidExtension(filename, getSupportedExtensions());
        }
    }

    @Override
    public String[] getSupportedMimeTypes() {
        List<String> mimeTypes = fileProperties.getAllowedMimeTypes().get("excel");
        return mimeTypes != null ? mimeTypes.toArray(new String[0]) : new String[0];
    }

    @Override
    public String[] getSupportedExtensions() {
        List<String> extensions = fileProperties.getAllowedExtensions().get("excel");
        return extensions != null ? extensions.toArray(new String[0]) : new String[0];
    }
}