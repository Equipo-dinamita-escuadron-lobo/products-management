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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;



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
     * Busca una unidad de medida por su ID y empresa.
     *
     * @param id el ID de la unidad de medida a buscar.
     * @param enterpriseId el ID de la empresa.
     * @return la unidad de medida encontrada.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     */
    @Override
    public UnitOfMeasure findByIdAndEnterpriseId(Long id, String enterpriseId) {
        return unitMeasurePersistencePort.findByIdAndEnterpriseId(id, enterpriseId).orElseThrow(UnitOfMeasureNotFoundException::new);
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
     * @param enterpriseId el ID de la empresa.
     * @param unitOfMeasure los datos de la unidad de medida actualizada.
     * @return la unidad de medida actualizada.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     * @throws UnitOfMeasureNameAlreadyExistsException si ya existe una unidad con el mismo nombre.
     * @throws UnitOfMeasureAbbreviationAlreadyExistsException si ya existe una unidad con la misma abreviación.
     */

    @Override
    public UnitOfMeasure update(Long id, String enterpriseId, UnitOfMeasure unitOfMeasure) {
        return unitMeasurePersistencePort.findByIdAndEnterpriseId(id, enterpriseId)
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
     * @param enterpriseId el ID de la empresa.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     */

    @Override
    public void changeState(Long id, String enterpriseId) {
        UnitOfMeasure unitOfMeasure = unitMeasurePersistencePort.findByIdAndEnterpriseId(id, enterpriseId)
                .orElseThrow(UnitOfMeasureNotFoundException::new);
        unitOfMeasure.setState(!unitOfMeasure.isState());
        unitMeasurePersistencePort.create(unitOfMeasure);
    }

    /**
     * Elimina una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a eliminar.
     * @param enterpriseId el ID de la empresa.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no se encuentra.
     * @throws UnitOfMeasureAssociatedException si la unidad de medida está asociada a productos.
     */

    @Override
    public void deleteById(Long id, String enterpriseId) {
        if (unitMeasurePersistencePort.findByIdAndEnterpriseId(id, enterpriseId).isEmpty()) {
            throw new UnitOfMeasureNotFoundException();
        }
        List<Product> products = productServicePort.findAllByUnitOfMeasure(id);
        if (!products.isEmpty()) {
            throw new UnitOfMeasureAssociatedException();
        }
        unitMeasurePersistencePort.deleteByIdAndEnterpriseId(id, enterpriseId);
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

    /**
     * Obtiene todas las unidades de medida de una empresa con paginación.
     *
     * @param enterpriseId el ID de la empresa
     * @param pageable información de paginación
     * @return página de unidades de medida encontradas (puede estar vacía si no hay datos)
     */
    @Override
    public Page<UnitOfMeasure> getAllUnitOfMeasuresBy(String enterpriseId, Pageable pageable) {
        return unitMeasurePersistencePort.getAllUnitOfMeasuresBy(enterpriseId, pageable);
    }

    /**
     * Cuenta el total de unidades de medida por empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @return el número total de unidades de medida
     */
    @Override
    public long countAllUnitOfMeasuresByEntId(String enterpriseId) {
        return unitMeasurePersistencePort.countByEnterpriseId(enterpriseId);
    }

    /**
     * Busca unidades de medida por empresa y término de búsqueda con ordenamiento.
     *
     * @param enterpriseId El id de la empresa
     * @param search Término de búsqueda
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de unidades de medida que coinciden con la búsqueda
     */
    @Override
    public Page<UnitOfMeasure> findByEntIdAndSearch(String enterpriseId, String search, int page, int size, String sortField, String sortOrder) {
        return unitMeasurePersistencePort.findByEnterpriseIdAndSearch(enterpriseId, search, page, size, sortField, sortOrder);
    }

    /**
     * Cuenta unidades de medida por empresa y término de búsqueda.
     *
     * @param enterpriseId El id de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de unidades de medida que coinciden
     */
    @Override
    public long countByEntIdAndSearch(String enterpriseId, String search) {
        return unitMeasurePersistencePort.countByEnterpriseIdAndSearch(enterpriseId, search);
    }

    /**
     * Obtiene todas las unidades de medida con ordenamiento.
     *
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de unidades de medida ordenadas
     */
    @Override
    public Page<UnitOfMeasure> getAllUnitOfMeasuresByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder) {
        return unitMeasurePersistencePort.getAllUnitOfMeasuresByWithSort(enterpriseId, page, size, sortField, sortOrder);
    }

    /**
     * Obtiene todas las unidades de medida activas con ordenamiento ascendente por nombre.
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @return Página de unidades de medida activas ordenadas por nombre ascendente
     */
    @Override
    public Page<UnitOfMeasure> getAllActiveUnitOfMeasuresBy(String enterpriseId, int page, int size) {
        return unitMeasurePersistencePort.getActiveUnitOfMeasuresBy(enterpriseId, page, size, "name", "asc");
    }

    /**
     * Cuenta el total de unidades de medida activas por empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @return el número total de unidades de medida activas
     */
    @Override
    public long countActiveUnitOfMeasuresByEntId(String enterpriseId) {
        return unitMeasurePersistencePort.countActiveByEnterpriseId(enterpriseId);
    }
}
