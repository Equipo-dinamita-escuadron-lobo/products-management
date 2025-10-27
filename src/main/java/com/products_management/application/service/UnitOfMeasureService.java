package com.products_management.application.service;

import com.products_management.application.ports.input.IUnitOfMeasureServicePort;
import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.exception.unitOfMeasure.UnitOfMeasureAbbreviationAlreadyExistsException;
import com.products_management.domain.exception.unitOfMeasure.UnitOfMeasureAssociatedException;
import com.products_management.domain.exception.unitOfMeasure.UnitOfMeasureNameAlreadyExistsException;
import com.products_management.domain.exception.unitOfMeasure.UnitOfMeasureNotFoundException;
import com.products_management.domain.model.Product;
import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.domain.utils.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;



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

    @Override
    public List<UnitOfMeasure> findAll(String enterpriseId) {
        return unitMeasurePersistencePort.findByEnterpriseId(enterpriseId).stream()
                .sorted(Comparator.comparing(UnitOfMeasure::getName)) // Ordenar alfabéticamente por nombre
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todas las unidades de medida activadas asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las unidades de medida activadas de la empresa.
     */

    @Override
    public List<UnitOfMeasure> findActivated(String enterpriseId) {
        return unitMeasurePersistencePort.findByEnterpriseIdAndState(enterpriseId, true);
    }

    /**
     * Crea una nueva unidad de medida.
     *
     * @param unitOfMeasure la unidad de medida a crear.
     * @return la unidad de medida creada.
     * @throws UnitOfMeasureNameAlreadyExistsException si ya existe una unidad con el mismo nombre.
     * @throws UnitOfMeasureAbbreviationAlreadyExistsException si ya existe una unidad con la misma abreviación.
     */

    @Override
    public UnitOfMeasure create(UnitOfMeasure unitOfMeasure) {
        // Normalizar nombre y abreviación de manera consistente (para validación y almacenamiento)
        unitOfMeasure.setName(StringNormalizer.normalize(unitOfMeasure.getName()));
        unitOfMeasure.setAbbreviation(StringNormalizer.normalize(unitOfMeasure.getAbbreviation()));
        validateUnitOfMeasureUniqueness(unitOfMeasure);
        return unitMeasurePersistencePort.create(unitOfMeasure);
    }

    /**
     * Actualiza una unidad de medida existente.
     *
     * @param id el ID de la unidad de medida a actualizar.
     * @param unitOfMeasure los datos de la unidad de medida actualizada.
     * @return la unidad de medida actualizada.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     * @throws UnitOfMeasureNameAlreadyExistsException si ya existe una unidad con el mismo nombre.
     * @throws UnitOfMeasureAbbreviationAlreadyExistsException si ya existe una unidad con la misma abreviación.
     */

    @Override
    public UnitOfMeasure update(Long id, UnitOfMeasure unitOfMeasure) {
        return unitMeasurePersistencePort.findById(id)
                .map(existingUnit -> {
                    // Normalizar nombre y abreviación de manera consistente (para validación y almacenamiento)
                    unitOfMeasure.setName(StringNormalizer.normalize(unitOfMeasure.getName()));
                    unitOfMeasure.setAbbreviation(StringNormalizer.normalize(unitOfMeasure.getAbbreviation()));
                    validateUnitOfMeasureUniquenessForUpdate(id, unitOfMeasure);
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

    @Override
    public void changeState(Long id) {
        UnitOfMeasure unitOfMeasure = unitMeasurePersistencePort.findById(id)
                .orElseThrow(() -> new UnitOfMeasureNotFoundException());
        unitOfMeasure.setState(!unitOfMeasure.isState());
        unitMeasurePersistencePort.create(unitOfMeasure);
    }

    /**
     * Elimina una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a eliminar.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     * @throws UnitOfMeasureAssociatedException si la unidad de medida está asociada a productos.
     */

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

    @Override
    public void deleteAll() {
        unitMeasurePersistencePort.deleteAll();
    }

    /**
     * Valida que el nombre y la abreviación de una unidad de medida sean únicos dentro de la empresa.
     *
     * @param unitOfMeasure la unidad de medida a validar.
     * @throws UnitOfMeasureNameAlreadyExistsException si ya existe una unidad con el mismo nombre.
     * @throws UnitOfMeasureAbbreviationAlreadyExistsException si ya existe una unidad con la misma abreviación.
     */
    private void validateUnitOfMeasureUniqueness(UnitOfMeasure unitOfMeasure) {
        // El nombre y abreviación ya están normalizados, se usan directamente para validación
        if (unitMeasurePersistencePort.existsByNameAndEnterpriseId(
                unitOfMeasure.getName(), unitOfMeasure.getEnterpriseId())) {
            throw new UnitOfMeasureNameAlreadyExistsException(unitOfMeasure.getName());
        }

        if (unitMeasurePersistencePort.existsByAbbreviationAndEnterpriseId(
                unitOfMeasure.getAbbreviation(), unitOfMeasure.getEnterpriseId())) {
            throw new UnitOfMeasureAbbreviationAlreadyExistsException(unitOfMeasure.getAbbreviation());
        }
    }

    /**
     * Valida que el nombre y la abreviación de una unidad de medida sean únicos dentro de la empresa
     * durante una actualización, excluyendo la unidad que se está actualizando.
     *
     * @param id el ID de la unidad de medida que se está actualizando.
     * @param unitOfMeasure la unidad de medida a validar.
     * @throws UnitOfMeasureNameAlreadyExistsException si ya existe otra unidad con el mismo nombre.
     * @throws UnitOfMeasureAbbreviationAlreadyExistsException si ya existe otra unidad con la misma abreviación.
     */
    private void validateUnitOfMeasureUniquenessForUpdate(Long id, UnitOfMeasure unitOfMeasure) {
        // El nombre y abreviación ya están normalizados, se usan directamente para validación
        if (unitMeasurePersistencePort.existsByNameAndEnterpriseIdAndIdNot(
                unitOfMeasure.getName(), unitOfMeasure.getEnterpriseId(), id)) {
            throw new UnitOfMeasureNameAlreadyExistsException(unitOfMeasure.getName());
        }

        if (unitMeasurePersistencePort.existsByAbbreviationAndEnterpriseIdAndIdNot(
                unitOfMeasure.getAbbreviation(), unitOfMeasure.getEnterpriseId(), id)) {
            throw new UnitOfMeasureAbbreviationAlreadyExistsException(unitOfMeasure.getAbbreviation());
        }
    }
}
