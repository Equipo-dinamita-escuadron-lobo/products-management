package com.products_management.application.ports.output;

import com.products_management.domain.model.UnitOfMeasure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interfaz que define los puertos de persistencia para el servicio de unidades de medida.
 * Los puertos de persistencia representan las operaciones de almacenamiento y recuperación
 * de datos relacionadas con las unidades de medida.
 */
public interface IUnitOfMeasurePersistencePort {

    /**
     * Busca una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a buscar.
     * @return un Optional que contiene la unidad de medida encontrada, o un Optional vacío si no se encuentra.
     */
    Optional<UnitOfMeasure> findById(Long id);


    /**
     * Obtiene todas las unidades de medida de una empresa con paginación.
     * @param enterpriseId El id de la empresa
     * @param pageable El objeto pageable
     * @return La página de unidades de medida
     */
    Page<UnitOfMeasure> getAllUnitOfMeasuresBy(String enterpriseId, Pageable pageable);

    /**
     * Obtiene todas las unidades de medida de una empresa filtradas por estado.
     * Optimizado para exportación con filtro de estado en BD.
     *
     * @param enterpriseId El identificador de la entidad
     * @param state Estado de las unidades de medida (true=activas, false=inactivas)
     * @param pageable El objeto Pageable que contiene la información de paginación
     * @return Una página de objetos UnitOfMeasure filtrados por estado
     */
    Page<UnitOfMeasure> getAllUnitOfMeasuresByState(String enterpriseId, Boolean state, Pageable pageable);

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
    Page<UnitOfMeasure> findByEnterpriseIdAndSearch(String enterpriseId, String search, int page, int size, String sortField, String sortOrder);

    /**
     * Cuenta unidades de medida por empresa y término de búsqueda.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de unidades de medida que coinciden
     */
    long countByEnterpriseIdAndSearch(String enterpriseId, String search);

    /**
     * Obtiene todas las unidades de medida con ordenamiento.
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de unidades de medida ordenadas
     */
    Page<UnitOfMeasure> getAllUnitOfMeasuresByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder);

    /**
     * Cuenta el total de unidades de medida por empresa.
     * @param enterpriseId El id de la empresa
     * @return El número total de unidades de medida
     */
    long countByEnterpriseId(String enterpriseId);

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
    Page<UnitOfMeasure> getActiveUnitOfMeasuresBy(String enterpriseId, int page, int size, String sortField, String sortOrder);

    /**
     * Cuenta el total de unidades de medida activas por empresa.
     *
     * @param enterpriseId ID de la empresa
     * @return Cantidad total de unidades de medida activas
     */
    long countActiveByEnterpriseId(String enterpriseId);

    /**
     * Crea una nueva unidad de medida.
     *
     * @param unitOfMeasure la unidad de medida a crear.
     * @return la unidad de medida creada.
     */
    UnitOfMeasure create(UnitOfMeasure unitOfMeasure);

    /**
     * Elimina una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a eliminar.
     */
    void deleteById(Long id);

    /**
     * Verifica si existe una unidad de medida con el nombre especificado para una empresa.
     *
     * @param name el nombre de la unidad de medida.
     * @param enterpriseId el ID de la empresa.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNameAndEnterpriseId(String name, String enterpriseId);
    
    /**
     * Verifica si existe una unidad de medida con la abreviación especificada para una empresa.
     *
     * @param abbreviation la abreviación de la unidad de medida.
     * @param enterpriseId el ID de la empresa.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByAbbreviationAndEnterpriseId(String abbreviation, String enterpriseId);
    
    /**
     * Verifica si existe una unidad de medida con el nombre especificado para una empresa, excluyendo un ID específico.
     *
     * @param name el nombre de la unidad de medida.
     * @param enterpriseId el ID de la empresa.
     * @param id el ID a excluir de la búsqueda.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id);
    
    /**
     * Verifica si existe una unidad de medida con la abreviación especificada para una empresa, excluyendo un ID específico.
     *
     * @param abbreviation la abreviación de la unidad de medida.
     * @param enterpriseId el ID de la empresa.
     * @param id el ID a excluir de la búsqueda.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByAbbreviationAndEnterpriseIdAndIdNot(String abbreviation, String enterpriseId, Long id);
}
