package com.products_management.infraestructure.input.rest.advice;

import com.products_management.domain.exception.*;
import com.products_management.domain.exception.category.CategoryAssociatedException;
import com.products_management.domain.exception.category.CategoryNameAlreadyExistsException;
import com.products_management.domain.exception.category.CategoryNotFoundException;
import com.products_management.domain.exception.product.ProductNotFoundException;
import com.products_management.domain.exception.productType.ProductTypeAssociatedException;
import com.products_management.domain.exception.productType.ProductTypeNameAlreadyExistsException;
import com.products_management.domain.exception.productType.ProductTypeNotFoundException;
import com.products_management.domain.exception.unitOfMeasure.UnitOfMeasureAbbreviationAlreadyExistsException;
import com.products_management.domain.exception.unitOfMeasure.UnitOfMeasureAssociatedException;
import com.products_management.domain.exception.unitOfMeasure.UnitOfMeasureNameAlreadyExistsException;
import com.products_management.domain.exception.unitOfMeasure.UnitOfMeasureNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Manejador global de excepciones para toda la aplicación.
 * Proporciona respuestas consistentes y descriptivas para diferentes tipos de errores.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja todas las excepciones de entidades no encontradas (NotFound).
     */
    @ExceptionHandler({
        ProductTypeNotFoundException.class,
        ProductNotFoundException.class,
        CategoryNotFoundException.class,
        UnitOfMeasureNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(
            BaseBusinessException ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .code(ex.getErrorCode().getCode())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja todas las excepciones de entidades asociadas (conflictos).
     */
    @ExceptionHandler({
        CategoryAssociatedException.class,
        UnitOfMeasureAssociatedException.class,
        ProductTypeAssociatedException.class
    })
    public ResponseEntity<ErrorResponse> handleAssociatedExceptions(
            BaseBusinessException ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflict")
                .message(ex.getMessage())
                .code(ex.getErrorCode().getCode())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Maneja todas las excepciones de duplicados (conflictos de unicidad).
     */
    @ExceptionHandler({
        UnitOfMeasureNameAlreadyExistsException.class,
        UnitOfMeasureAbbreviationAlreadyExistsException.class,
        CategoryNameAlreadyExistsException.class,
        ProductTypeNameAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleDuplicateExceptions(
            BaseBusinessException ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Duplicate Entry")
                .message(ex.getMessage())
                .code(ex.getErrorCode().getCode())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones generales no específicas.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Ha ocurrido un error interno del servidor")
                .code(ErrorCode.GENERIC_ERROR.getCode())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}