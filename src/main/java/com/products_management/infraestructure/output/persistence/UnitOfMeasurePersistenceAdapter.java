package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IUnitOfMeasurePersistenceMapper;
import com.products_management.infraestructure.output.persistence.repository.IUnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
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
     * Obtiene una lista de todas las unidades de medida.
     *
     * @return una lista de unidades de medida
     */
    @Override
    public List<UnitOfMeasure> findAll() {
        return unitOfMeasurePersistenceMapper.toUnitOfMeasureList(unitOfMeasureRepository.findAll());
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
     * Busca unidades de medida por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @return una lista de unidades de medida de la empresa
     */
    @Override
    public List<UnitOfMeasure> findByEnterpriseId(String enterpriseId) {
        return unitOfMeasurePersistenceMapper.toUnitOfMeasureList(
                unitOfMeasureRepository.findByEnterpriseId(enterpriseId));
    }

    /**
     * Busca unidades de medida activas por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @param state el estado de la unidad de medida
     * @return una lista de unidades de medida activas de la empresa
     */
    @Override
    public List<UnitOfMeasure> findByEnterpriseIdAndState(String enterpriseId, boolean state) {
        return unitOfMeasurePersistenceMapper.toUnitOfMeasureList(
                unitOfMeasureRepository.findByEnterpriseIdAndState(enterpriseId, state));
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
}
