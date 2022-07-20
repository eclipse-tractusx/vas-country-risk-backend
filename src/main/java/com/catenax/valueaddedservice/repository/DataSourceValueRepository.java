package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.DataSourceValue;
import com.catenax.valueaddedservice.dto.DashBoardTableDTO;
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

    @Query("select new com.catenax.valueaddedservice.dto.DashBoardTableDTO(dsv.id,dsv.country,dsv.score,ds.dataSourceName) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and ds.dataSourceName in ?3 "+
            "and ds.yearPublished = ?4 "+
            "and dsv.score > ?1 ")
    List<DashBoardTableDTO> findByRatingAndCountryAndScoreGreaterThanAndYear(Float score, List<String> country,List<String> dataSources,Integer year);

    @Query("select new com.catenax.valueaddedservice.dto.DashBoardTableDTO(dsv.id,dsv.country,dsv.score,ds.dataSourceName) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and ds.dataSourceName in ?3 "+
            "and dsv.score > ?1 ")
    List<DashBoardTableDTO> findByRatingAndCountryAndScoreGreaterThan(Float score, List<String> country,List<String> dataSources);

    @Query("select new com.catenax.valueaddedservice.dto.DashBoardTableDTO(dsv.id,dsv.country,dsv.score,ds.dataSourceName) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and ds.dataSourceName in ?1")
    List<DashBoardTableDTO> findByRating(List<String> dataSources);

    @Query("select new com.catenax.valueaddedservice.dto.DashBoardTableDTO(dsv.id,dsv.country,dsv.score,ds.dataSourceName) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and ds.yearPublished = ?3 "+
            "and dsv.score > ?1 ")
    List<DashBoardTableDTO> findByCountryScoreGreaterThanAndYear(Float score, List<String> country,Integer year);

    @Query("select new com.catenax.valueaddedservice.dto.DashBoardTableDTO(dsv.id,dsv.country,dsv.score,ds.dataSourceName) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and dsv.score > ?1 ")
    List<DashBoardTableDTO> findByCountryScoreGreaterThan(Float score, List<String> country);




}
