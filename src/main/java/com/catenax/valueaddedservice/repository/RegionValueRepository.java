package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.RegionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RegionValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegionValueRepository extends JpaRepository<RegionValue, Long> {}
