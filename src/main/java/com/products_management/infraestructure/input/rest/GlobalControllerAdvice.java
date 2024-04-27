package com.products_management.infraestructure.input.rest;

import com.products_management.domain.exception.CategoryNotFoundException;
import com.products_management.domain.exception.UnitOfMeasureNotFoundException;
import com.products_management.domain.model.Category;
import com.products_management.domain.model.ErrorResponse;
import com.products_management.domain.model.Product;
import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.domain.exception.ProductNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.products_management.utils.ErrorCatalog.*;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handlerProductNotFoundException() {
        return ErrorResponse.builder()
                .code(PRODUCT_NOT_FOUND.getCode())
                .message(PRODUCT_NOT_FOUND.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UnitOfMeasureNotFoundException.class)
    public ErrorResponse handlerUnitOfMeasureNotFoundException() {
        return ErrorResponse.builder()
                .code(UNITOFMEASURE_NOT_FOUND.getCode())
                .message(UNITOFMEASURE_NOT_FOUND.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorResponse handlerCategoryNotFoundException() {
        return ErrorResponse.builder()
                .code(CATEGORY_NOT_FOUND.getCode())
                .message(CATEGORY_NOT_FOUND.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(
            MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        if (exception.getBindingResult().getTarget() instanceof Product) {
            return ErrorResponse.builder()
                    .code(INVALID_PRODUCT.getCode())
                    .message(INVALID_PRODUCT.getMessage())
                    .details(bindingResult.getFieldErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.toList()))
                    .timestamp(LocalDateTime.now())
                    .build();
        } else if (exception.getBindingResult().getTarget() instanceof UnitOfMeasure) {
            return ErrorResponse.builder()
                    .code(INVALID_UNITOFMEASURE.getCode())
                    .message(INVALID_UNITOFMEASURE.getMessage())
                    .details(bindingResult.getFieldErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.toList()))
                    .timestamp(LocalDateTime.now())
                    .build();
            }else if (exception.getBindingResult().getTarget() instanceof Category) {
            return ErrorResponse.builder()
                    .code(INVALID_CATEGORY.getCode())
                    .message(INVALID_CATEGORY.getMessage())
                    .details(bindingResult.getFieldErrors()
                            .stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.toList()))
                    .timestamp(LocalDateTime.now())
                    .build();
        }else {
            return ErrorResponse.builder()
                    .code(GENERIC_ERROR.getCode())
                    .message(GENERIC_ERROR.getMessage())
                    .details(Collections.singletonList(exception.getMessage()))
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception exception) {
        return ErrorResponse.builder()
                .code(GENERIC_ERROR.getCode())
                .message(GENERIC_ERROR.getMessage())
                .details(Collections.singletonList(exception.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
    }

}
