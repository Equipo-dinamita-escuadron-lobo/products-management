package com.products_management.application.ports.output;

import com.products_management.domain.model.UnitOfMeasure;
import java.util.List;
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
     * Obtiene una lista de todas las unidades de medida.
     *
     * @return una lista de todas las unidades de medida.
     */
    List<UnitOfMeasure> findAll();
    
    /**
     * Busca unidades de medida por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de unidades de medida de la empresa.
     */
    List<UnitOfMeasure> findByEnterpriseId(String enterpriseId);
    
    /**
     * Busca unidades de medida activas por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param state el estado de la unidad de medida.
     * @return una lista de unidades de medida activas de la empresa.
     */
    List<UnitOfMeasure> findByEnterpriseIdAndState(String enterpriseId, boolean state);

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
