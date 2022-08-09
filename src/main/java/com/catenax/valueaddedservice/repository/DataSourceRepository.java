package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.CompanyUser;
import com.catenax.valueaddedservice.domain.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the DataSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {

    List<DataSource> findByYearPublished(Integer year);

    List<DataSource> findByCompanyUserAndDataSourceName(CompanyUser user, String name);
}
