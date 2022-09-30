package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Country entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findByCountryIn(List<String> stringList);

    Optional<Country> findByCountry(String countryName);
}
