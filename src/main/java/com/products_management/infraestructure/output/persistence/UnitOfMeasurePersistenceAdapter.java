package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IUnitOfMeasurePersistenceMapper;
import com.products_management.infraestructure.output.persistence.repository.IUnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Adaptador de persistencia para la entidad Unidad de Medida.
 * Implementa la interfaz IUnitOfMeasurePersistencePort para proporcionar métodos de persistencia.
 */
@Component
@RequiredArgsConstructor
public class UnitOfMeasurePersistenceAdapter implements IUnitOfMeasurePersistencePort {

    private final IUnitOfMeasureRepository unitOfMeasureRepository;
    private final IUnitOfMeasurePersistenceMapper unitOfMeasurePersistenceMapper;

    /**
     * Busca una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida
     * @return un Optional que contiene la unidad de medida si se encuentra, de lo contrario vacío
     */
    @Override
    public Optional<UnitOfMeasure> findById(Long id) {
        return unitOfMeasureRepository.findById(Long.valueOf(id))
                .map(unitOfMeasurePersistenceMapper::toUnitOfMeasure);
    }

    /**
     * Crea una nueva unidad de medida.
     *
     * @param unitOfMeasure la unidad de medida a crear
     * @return la unidad de medida creada
     */
    @Override
    public UnitOfMeasure create(UnitOfMeasure unitOfMeasure) {
        return unitOfMeasurePersistenceMapper.toUnitOfMeasure(unitOfMeasureRepository.save(unitOfMeasurePersistenceMapper.toUnitOfMeasureEntity(unitOfMeasure)));
    }

    /**
     * Elimina una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a eliminar
     */
    @Override
    public void deleteById(Long id) {
        unitOfMeasureRepository.deleteById(Long.valueOf(id));
    }

    /**
     * Verifica si existe una unidad de medida con el nombre especificado para una empresa.
     *
     * @param name el nombre de la unidad de medida
     * @param enterpriseId el ID de la empresa
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existsByNameAndEnterpriseId(String name, String enterpriseId) {
        return unitOfMeasureRepository.existsByNameAndEnterpriseId(name, enterpriseId);
    }

    /**
     * Verifica si existe una unidad de medida con la abreviación especificada para una empresa.
     *
     * @param abbreviation la abreviación de la unidad de medida
     * @param enterpriseId el ID de la empresa
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existsByAbbreviationAndEnterpriseId(String abbreviation, String enterpriseId) {
        return unitOfMeasureRepository.existsByAbbreviationAndEnterpriseId(abbreviation, enterpriseId);
    }

    /**
     * Verifica si existe una unidad de medida con el nombre especificado para una empresa, excluyendo un ID específico.
     *
     * @param name el nombre de la unidad de medida
     * @param enterpriseId el ID de la empresa
     * @param id el ID a excluir de la búsqueda
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id) {
        return unitOfMeasureRepository.existsByNameAndEnterpriseIdAndIdNot(name, enterpriseId, id);
    }

    /**
     * Verifica si existe una unidad de medida con la abreviación especificada para una empresa, excluyendo un ID específico.
     *
     * @param abbreviation la abreviación de la unidad de medida
     * @param enterpriseId el ID de la empresa
     * @param id el ID a excluir de la búsqueda
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existsByAbbreviationAndEnterpriseIdAndIdNot(String abbreviation, String enterpriseId, Long id) {
        return unitOfMeasureRepository.existsByAbbreviationAndEnterpriseIdAndIdNot(abbreviation, enterpriseId, id);
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
        Page<UnitOfMeasureEntity> pageEntities = unitOfMeasureRepository.getUnitOfMeasuresBy(enterpriseId, pageable);
        Page<UnitOfMeasure> pageUnitOfMeasures = pageEntities.map(this::convertToUnitOfMeasure);

        return pageUnitOfMeasures;
    }

    /**
     * Obtiene todas las unidades de medida de una empresa filtradas por estado.
     * Optimizado para exportación: el filtro se aplica en BD, no en memoria.
     *
     * @param enterpriseId El identificador de la entidad
     * @param state Estado de las unidades de medida (true=activas, false=inactivas)
     * @param pageable El objeto Pageable que contiene la información de paginación
     * @return Una página de objetos UnitOfMeasure filtrados por estado
     */
    @Override
    public Page<UnitOfMeasure> getAllUnitOfMeasuresByState(String enterpriseId, Boolean state, Pageable pageable) {
        Page<UnitOfMeasureEntity> pageEntities = unitOfMeasureRepository.getUnitOfMeasuresByEnterpriseIdAndState(enterpriseId, state, pageable);
        Page<UnitOfMeasure> pageUnitOfMeasures = pageEntities.map(this::convertToUnitOfMeasure);

        return pageUnitOfMeasures;
    }

    /**
     * Busca unidades de medida por empresa y término de búsqueda con ordenamiento.
     * Busca en: nombres, abreviaturas.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de unidades de medida que coinciden con la búsqueda
     */
    @Override
    public Page<UnitOfMeasure> findByEnterpriseIdAndSearch(String enterpriseId, String search, int page, int size, String sortField, String sortOrder) {
        String entitySortField = mapUnitOfMeasureSortField(sortField);
        Sort sort = "desc".equalsIgnoreCase(sortOrder) 
            ? Sort.by(entitySortField).descending() 
            : Sort.by(entitySortField).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UnitOfMeasureEntity> pageEntities = unitOfMeasureRepository.findByEnterpriseIdAndSearch(enterpriseId, search, pageable);
        Page<UnitOfMeasure> pageUnitOfMeasures = pageEntities.map(this::convertToUnitOfMeasure);

        return pageUnitOfMeasures;
    }

    /**
     * Cuenta unidades de medida por empresa y término de búsqueda.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de unidades de medida que coinciden
     */
    @Override
    public long countByEnterpriseIdAndSearch(String enterpriseId, String search) {
        return unitOfMeasureRepository.countByEnterpriseIdAndSearch(enterpriseId, search);
    }

    /**
     * Obtiene todas las unidades de medida con ordenamiento.
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de unidades de medida ordenadas
     */
    @Override
    public Page<UnitOfMeasure> getAllUnitOfMeasuresByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder) {
        String entitySortField = mapUnitOfMeasureSortField(sortField);
        Sort sort = "desc".equalsIgnoreCase(sortOrder) 
            ? Sort.by(entitySortField).descending() 
            : Sort.by(entitySortField).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UnitOfMeasureEntity> pageEntities = unitOfMeasureRepository.getUnitOfMeasuresBy(enterpriseId, pageable);
        Page<UnitOfMeasure> pageUnitOfMeasures = pageEntities.map(this::convertToUnitOfMeasure);

        return pageUnitOfMeasures;
    }

    /**
     * Cuenta el total de unidades de medida por empresa.
     * @param enterpriseId El id de la empresa
     * @return El número total de unidades de medida
     */
    @Override
    public long countByEnterpriseId(String enterpriseId) {
        return unitOfMeasureRepository.countByEnterpriseId(enterpriseId);
    }

    /**
     * Obtiene todas las unidades de medida activas de una empresa con ordenamiento.
     *
     * @param enterpriseId ID de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de unidades de medida activas ordenadas
     */
    @Override
    public Page<UnitOfMeasure> getActiveUnitOfMeasuresBy(String enterpriseId, int page, int size, String sortField, String sortOrder) {
        String entitySortField = mapUnitOfMeasureSortField(sortField);
        Sort sort = "desc".equalsIgnoreCase(sortOrder) 
            ? Sort.by(entitySortField).descending() 
            : Sort.by(entitySortField).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UnitOfMeasureEntity> pageEntities = unitOfMeasureRepository.getActiveUnitOfMeasuresBy(enterpriseId, pageable);
        Page<UnitOfMeasure> pageUnitOfMeasures = pageEntities.map(this::convertToUnitOfMeasure);

        return pageUnitOfMeasures;
    }

    /**
     * Cuenta el total de unidades de medida activas por empresa.
     *
     * @param enterpriseId ID de la empresa
     * @return Cantidad total de unidades de medida activas
     */
    @Override
    public long countActiveByEnterpriseId(String enterpriseId) {
        return unitOfMeasureRepository.countActiveByEnterpriseId(enterpriseId);
    }

    /**
     * Mapea el campo de ordenamiento del modelo UnitOfMeasure al campo correspondiente en UnitOfMeasureEntity.
     * Solo permite ordenamiento por nombre y abreviación.
     * @param sortField Campo de ordenamiento del modelo
     * @return Campo de ordenamiento de la entidad
     */
    private String mapUnitOfMeasureSortField(String sortField) {
        if (sortField == null || sortField.trim().isEmpty()) {
            return "name"; // Default
        }
        switch (sortField.toLowerCase()) {
            case "name":
                return "name";
            case "abbreviation":
                return "abbreviation";
            default:
                return "name"; // Default para cualquier otro campo
        }
    }

    /**
     * Convierte un objeto UnitOfMeasureEntity a un objeto UnitOfMeasure.
     * @param unitOfMeasureEntity El objeto UnitOfMeasureEntity que se va a convertir.
     * @return El objeto UnitOfMeasure resultante de la conversión.
     */
    private UnitOfMeasure convertToUnitOfMeasure(UnitOfMeasureEntity unitOfMeasureEntity) {
        return unitOfMeasurePersistenceMapper.toUnitOfMeasure(unitOfMeasureEntity);
    }
}
