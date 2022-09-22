package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.Range;
import com.catenax.valueaddedservice.domain.enumeration.RangeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Range entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RangeRepository extends JpaRepository<Range, Long> {

    List<Range> findByCompanyUserNameAndCompanyUserEmailAndCompanyUserCompany(String name, String email,String company);

    @Modifying
    @Query("update Range u set u.value = :value where u.range = :range and u.companyUser.id = :companyUserId")
    void setValueForRange(@Param("value") Integer value, @Param("range") RangeType range ,@Param("companyUserId") Long companyUserId);


}
