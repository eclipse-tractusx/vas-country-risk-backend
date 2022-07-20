package com.catenax.valueaddedservice.repository;

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

    @Query(value = "SELECT * FROM T_Data_Source ds WHERE ds.year_Published = ?1", nativeQuery = true)
    List<DataSource> findRatingsByYear(Integer year);

}
