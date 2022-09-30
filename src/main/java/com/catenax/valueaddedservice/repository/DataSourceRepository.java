package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.DataSource;
import com.catenax.valueaddedservice.domain.enumeration.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the DataSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {

    List<DataSource> findByYearPublishedAndType(Integer year, Type type);

    List<DataSource> findByYearPublishedAndCompanyUserNameAndCompanyUserEmailAndCompanyUserCompany(Integer year,String name,String email,String company);

    List<DataSource> findByCompanyUserNameAndCompanyUserEmailAndCompanyUserCompanyOrType(String name,String email,String company,Type type);

}
