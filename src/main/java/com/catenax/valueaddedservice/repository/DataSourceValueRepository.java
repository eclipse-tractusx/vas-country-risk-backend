package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.DataSourceValue;
import com.catenax.valueaddedservice.dto.DashBoardDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the DataSourceValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSourceValueRepository extends JpaRepository<DataSourceValue, Long> {
    @Query("select new com.catenax.valueaddedservice.dto.DashBoardDTO(dsv.id,dsv.country,dsv.score,ds.dataSourceName) from DataSource ds LEFT JOIN DataSourceValue dsv ON dsv.dataSource.id = ds.id " +
            "and dsv.score > ?1")
    List<DashBoardDTO> findByScoreGreaterThan(Float score);

}
