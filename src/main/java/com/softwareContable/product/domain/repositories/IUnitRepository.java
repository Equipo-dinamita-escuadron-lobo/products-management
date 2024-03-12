package com.softwareContable.product.domain.repositories;

import com.softwareContable.product.domain.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUnitRepository extends JpaRepository<Unit, Long> {
}
