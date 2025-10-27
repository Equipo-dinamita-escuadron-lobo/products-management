package com.products_management.application.ports.input;

import com.products_management.domain.model.UnitOfMeasure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz que define los puertos de entrada para el servicio de unidades de medida.
 * Los puertos de entrada representan las operaciones que pueden ser realizadas
 * sobre las unidades de medida en la aplicación.
 */
public interface IUnitOfMeasureServicePort {

    /**
     * Busca una unidad de medida por su ID y empresa.
     *
     * @param id el ID de la unidad de medida a buscar.
     * @param enterpriseId el ID de la empresa.
     * @return la unidad de medida encontrada.
     */
    UnitOfMeasure findByIdAndEnterpriseId(Long id, String enterpriseId);

    /**
     * Obtiene todas las unidades de medida de una empresa con paginación.
     * @param enterpriseId El id de la empresa
     * @param pageable El objeto pageable
     * @return La página de unidades de medida
     */
    Page<UnitOfMeasure> getAllUnitOfMeasuresBy(String enterpriseId, Pageable pageable);

    /**
     * Cuenta el total de unidades de medida por empresa.
     * @param enterpriseId El id de la empresa
     * @return El número total de unidades de medida
     */
    long countAllUnitOfMeasuresByEntId(String enterpriseId);

    /**
     * Busca unidades de medida por empresa y término de búsqueda con ordenamiento.
     * @param enterpriseId El id de la empresa
     * @param search Término de búsqueda
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de unidades de medida que coinciden con la búsqueda
     */
    Page<UnitOfMeasure> findByEntIdAndSearch(String enterpriseId, String search, int page, int size, String sortField, String sortOrder);

    /**
     * Cuenta unidades de medida por empresa y término de búsqueda.
     * @param enterpriseId El id de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de unidades de medida que coinciden
     */
    long countByEntIdAndSearch(String enterpriseId, String search);

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
     * Obtiene todas las unidades de medida activas con ordenamiento ascendente por nombre.
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @return Página de unidades de medida activas ordenadas por nombre ascendente
     */
    Page<UnitOfMeasure> getAllActiveUnitOfMeasuresBy(String enterpriseId, int page, int size);

    /**
     * Cuenta el total de unidades de medida activas por empresa.
     * @param enterpriseId El id de la empresa
     * @return El número total de unidades de medida activas
     */
    long countActiveUnitOfMeasuresByEntId(String enterpriseId);


    /**
     * Crea una nueva unidad de medida.
     *
     * @param unitOfMeasure la unidad de medida a crear.
     * @return la unidad de medida creada.
     */
    UnitOfMeasure create(UnitOfMeasure unitOfMeasure);

    /**
     * Actualiza una unidad de medida existente.
     *
     * @param id el ID de la unidad de medida a actualizar.
     * @param enterpriseId el ID de la empresa.
     * @param unitOfMeasure los datos de la unidad de medida actualizada.
     * @return la unidad de medida actualizada.
     */
    UnitOfMeasure update(Long id, String enterpriseId, UnitOfMeasure unitOfMeasure);

    /**
     * Elimina una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a eliminar.
     * @param enterpriseId el ID de la empresa.
     */
    void deleteById(Long id, String enterpriseId);

    /**
     * Cambia el estado de una unidad de medida (por ejemplo, activado/desactivado).
     *
     * @param id el ID de la unidad de medida cuyo estado se va a cambiar.
     * @param enterpriseId el ID de la empresa.
     */
    void changeState(Long id, String enterpriseId);
}
