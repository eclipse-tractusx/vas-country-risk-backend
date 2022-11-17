package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.CompanyGates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the CompanyGates entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyGatesRepository extends JpaRepository<CompanyGates, Long> {

    List<CompanyGates> findByCompanyGroupId(Long companyGroupId);
}
