package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.CompanyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompanyGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyGroupRepository extends JpaRepository<CompanyGroup, Long> {}
