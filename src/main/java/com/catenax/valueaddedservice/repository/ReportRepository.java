package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.Report;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Report entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByCompanyUserNameAndCompanyAndType(String name, String company, Type type);
    List<Report> findByType(Type type);

    List<Report> findByCompanyAndType(String company, Type type);
}
