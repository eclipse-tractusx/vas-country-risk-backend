package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.ReportValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReportValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportValuesRepository extends JpaRepository<ReportValues, Long> {}
