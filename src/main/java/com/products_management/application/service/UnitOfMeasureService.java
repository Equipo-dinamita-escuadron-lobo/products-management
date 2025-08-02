package com.products_management.application.service;

import com.products_management.application.ports.input.IUnitOfMeasureServicePort;
import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.exception.UnitOfMeasureAssociatedException;
import com.products_management.domain.exception.UnitOfMeasureNotFoundException;
import com.products_management.domain.model.Product;
import com.products_management.domain.model.UnitOfMeasure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

import io.swagger.v3.oas.annotations.Operation; 
import io.swagger.v3.oas.annotations.Parameter; 
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Servicio que implementa la lógica de negocio para las unidades de medida.
 * Esta clase interactúa con los puertos de persistencia y realiza las operaciones
 * necesarias para gestionar las unidades de medida.
 */
@Service
@RequiredArgsConstructor
public class UnitOfMeasureService implements IUnitOfMeasureServicePort {

    private final IUnitOfMeasurePersistencePort unitMeasurePersistencePort;
    private final ProductService productServicePort;

    /**
     * Busca una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a buscar.
     * @return la unidad de medida encontrada.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     */
    @Operation(summary = "Find a unit of measure by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Unit of measure found"),
        @ApiResponse(responseCode = "404", description = "Unit of measure not found")
    })
    @Parameter(name = "id", description = "Unit of measure ID", required = true)
    @Override
    public UnitOfMeasure findById(Long id) {
        return unitMeasurePersistencePort.findById(id).orElseThrow(UnitOfMeasureNotFoundException::new);
    }

    /**
     * Obtiene una lista de todas las unidades de medida asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las unidades de medida de la empresa.
     */
    @Operation(summary = "Find all units of measure by enterprise ID", responses = {
        @ApiResponse(responseCode = "200", description = "Units of measure found")
    })
    @Parameter(name = "enterpriseId", description = "Enterprise ID", required = true)
    @Override
    public List<UnitOfMeasure> findAll(String enterpriseId) {
        List<UnitOfMeasure> allUnitOfMeasure = unitMeasurePersistencePort.findAll();
        return allUnitOfMeasure.stream()
                .filter(unitOfMeasure -> unitOfMeasure.getEnterpriseId().equals(enterpriseId) || unitOfMeasure.getEnterpriseId().equals("standart"))
                .sorted(Comparator.comparing(UnitOfMeasure::getName)) // Ordenar alfabéticamente por nombre
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todas las unidades de medida activadas asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las unidades de medida activadas de la empresa.
     */
    @Operation(summary = "Find all activated units of measure by enterprise ID", responses = {
        @ApiResponse(responseCode = "200", description = "Units of measure found")
    })
    @Parameter(name = "enterpriseId", description = "Enterprise ID", required = true)
    @Override
    public List<UnitOfMeasure> findActivated(String enterpriseId) {
        List<UnitOfMeasure> allUnitOfMeasure = unitMeasurePersistencePort.findAll();
        return allUnitOfMeasure.stream()
                .filter(unitOfMeasure -> "true".equals(unitOfMeasure.getState()))
                .filter(unitOfMeasure -> unitOfMeasure.getEnterpriseId().equals(enterpriseId) || unitOfMeasure.getEnterpriseId().equals("standart"))
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva unidad de medida.
     *
     * @param unitOfMeasure la unidad de medida a crear.
     * @return la unidad de medida creada.
     */
    @Operation(summary = "Create a unit of measure", responses = {
        @ApiResponse(responseCode = "200", description = "Unit of measure created")
    })
    @Parameter(name = "unitOfMeasure", description = "Unit of measure to create", required = true)
    @Override
    public UnitOfMeasure create(UnitOfMeasure unitOfMeasure) {
        return unitMeasurePersistencePort.create(unitOfMeasure);
    }

    /**
     * Actualiza una unidad de medida existente.
     *
     * @param id el ID de la unidad de medida a actualizar.
     * @param unitOfMeasure los datos de la unidad de medida actualizada.
     * @return la unidad de medida actualizada.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     */
    @Operation(summary = "Update a unit of measure", responses = {
        @ApiResponse(responseCode = "200", description = "Unit of measure updated"),
        @ApiResponse(responseCode = "404", description = "Unit of measure not found")
    })
    @Parameter(name = "id", description = "Unit of measure ID", required = true)
    @Parameter(name = "unitOfMeasure", description = "Unit of measure to update", required = true)
    @Override
    public UnitOfMeasure update(Long id, UnitOfMeasure unitOfMeasure) {
        return unitMeasurePersistencePort.findById(id)
                .map(existingUnit -> {
                    existingUnit.setName(unitOfMeasure.getName());
                    existingUnit.setDescription(unitOfMeasure.getDescription());
                    existingUnit.setAbbreviation(unitOfMeasure.getAbbreviation());
                    return unitMeasurePersistencePort.create(existingUnit);
                })
                .orElseThrow(UnitOfMeasureNotFoundException::new);
    }

    /**
     * Cambia el estado de una unidad de medida (activado/desactivado).
     *
     * @param id el ID de la unidad de medida cuyo estado se va a cambiar.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     */
    @Operation(summary = "Change the state of a unit of measure", responses = {
        @ApiResponse(responseCode = "200", description = "Unit of measure state changed"),
        @ApiResponse(responseCode = "404", description = "Unit of measure not found")
    })
    @Parameter(name = "id", description = "Unit of measure ID", required = true)
    @Override
    public void changeState(Long id) {
        UnitOfMeasure unitOfMeasure = unitMeasurePersistencePort.findById(id)
                .orElseThrow(() -> new UnitOfMeasureNotFoundException());
        unitOfMeasure.setState("true".equals(unitOfMeasure.getState()) ? "false" : "true");
        unitMeasurePersistencePort.create(unitOfMeasure);
    }

    /**
     * Elimina una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a eliminar.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     * @throws UnitOfMeasureAssociatedException si la unidad de medida está asociada a productos.
     */
    @Operation(summary = "Delete a unit of measure by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Unit of measure deleted"),
        @ApiResponse(responseCode = "404", description = "Unit of measure not found"),
        @ApiResponse(responseCode = "409", description = "Unit of measure associated")
    })
    @Parameter(name = "id", description = "Unit of measure ID", required = true)
    @Override
    public void deleteById(Long id) {
        if (unitMeasurePersistencePort.findById(id).isEmpty()) {
            throw new UnitOfMeasureNotFoundException();
        }
        List<Product> products = productServicePort.findAllByUnitOfMeasure(id);
        if (!products.isEmpty()) {
            throw new UnitOfMeasureAssociatedException();
        }
        unitMeasurePersistencePort.deleteById(id);
    }

    /**
     * Elimina todas las unidades de medida.
     */
    @Operation(summary = "Delete all units of measure", responses = {
        @ApiResponse(responseCode = "200", description = "Units of measure deleted")
    })
    @Override
    public void deleteAll() {
        unitMeasurePersistencePort.deleteAll();
    }
}
