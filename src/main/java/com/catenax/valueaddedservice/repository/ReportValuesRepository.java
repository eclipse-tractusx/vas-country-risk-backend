package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.Report;
import com.catenax.valueaddedservice.domain.ReportValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the ReportValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportValuesRepository extends JpaRepository<ReportValues, Long> {

    List<ReportValues> findByReport(Report report);
}
