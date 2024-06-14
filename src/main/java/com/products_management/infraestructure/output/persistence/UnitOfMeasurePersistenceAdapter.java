package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.output.persistence.mapper.impl.UnitOfMeasurePersistenceMapperImpl;
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
    private final UnitOfMeasurePersistenceMapperImpl unitOfMeasurePersistenceMapper;

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
     * Elimina todas las unidades de medida.
     */
    @Override
    public void deleteAll() {
        unitOfMeasureRepository.deleteAll();
    }
}
