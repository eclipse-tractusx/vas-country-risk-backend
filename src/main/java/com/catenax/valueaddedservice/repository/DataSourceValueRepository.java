package com.catenax.valueaddedservice.repository;

import com.catenax.valueaddedservice.domain.DataSourceValue;
import com.catenax.valueaddedservice.dto.DataDTO;
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

    @Query("select new com.catenax.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and ds.dataSourceName in ?3 "+
            "and ds.yearPublished = ?4 "+
            "and dsv.score > ?1 ")
    List<DataDTO> findByRatingAndCountryAndScoreGreaterThanAndYear(Float score, List<String> country, List<String> dataSources, Integer year);

    @Query("select new com.catenax.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and ds.dataSourceName in ?3 "+
            "and dsv.score > ?1 ")
    List<DataDTO> findByRatingAndCountryAndScoreGreaterThan(Float score, List<String> country,List<String> dataSources);


    @Query("select new com.catenax.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and ds.dataSourceName in ?1 ")
    List<DataDTO> findByRating(List<String> dataSources);

    @Query("select new com.catenax.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and ds.dataSourceName in ?1 " +
            "and ds.yearPublished = ?2 "+
            "and dsv.score > ?3 ")
    List<DataDTO> findByRatingAndScoreGreaterThanAndYear(List<String> dataSources,Integer year,Float score);

    @Query("select new com.catenax.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and ds.dataSourceName in ?1 " +
            "and dsv.score > ?2")
    List<DataDTO> findByRatingAndScoreGreaterThan(List<String> dataSources,Float score);
    @Query("select new com.catenax.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and ds.yearPublished = ?3 "+
            "and dsv.score > ?1 ")
    List<DataDTO> findByCountryScoreGreaterThanAndYear(Float score, List<String> country,Integer year);

    @Query("select new com.catenax.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and dsv.score > ?1 ")
    List<DataDTO> findByCountryScoreGreaterThan(Float score, List<String> country);

}
