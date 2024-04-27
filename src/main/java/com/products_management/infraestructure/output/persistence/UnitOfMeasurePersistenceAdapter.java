package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.output.persistence.mapper.impl.UnitOfMeasurePersistenceMapperImpl;
import com.products_management.infraestructure.output.persistence.repository.IUnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UnitOfMeasurePersistenceAdapter implements IUnitOfMeasurePersistencePort {

    private final IUnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasurePersistenceMapperImpl unitOfMeasurePersistenceMapper;

    @Override
    public Optional<UnitOfMeasure> findById(Long id) {
        return unitOfMeasureRepository.findById(Long.valueOf(id))
                .map(unitOfMeasurePersistenceMapper::toUnitOfMeasure);
    }

    @Override
    public List<UnitOfMeasure> findAll() {
        return unitOfMeasurePersistenceMapper.toUnitOfMeasureList(unitOfMeasureRepository.findAll());
    }

    @Override
    public UnitOfMeasure create(UnitOfMeasure unitOfMeasure) {
        return unitOfMeasurePersistenceMapper.toUnitOfMeasure(unitOfMeasureRepository.save(unitOfMeasurePersistenceMapper.toUnitOfMeasureEntity(unitOfMeasure)));
    }

    @Override
    public void deleteById(Long id) {
        unitOfMeasureRepository.deleteById(Long.valueOf(id));

    }
}
