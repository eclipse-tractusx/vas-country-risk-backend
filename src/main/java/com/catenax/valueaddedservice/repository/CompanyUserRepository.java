package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the CompanyUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {

     CompanyUser findByNameAndEmailAndCompanyName(String name, String email, String company);

     List<CompanyUser> findByCompanyName(String companyName);
}
