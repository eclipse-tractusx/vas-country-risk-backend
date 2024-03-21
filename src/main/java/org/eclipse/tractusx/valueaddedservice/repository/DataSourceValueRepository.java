/********************************************************************************
* Copyright (c) 2022,2024 BMW Group AG 
* Copyright (c) 2022,2024 Contributors to the Eclipse Foundation
*
* See the NOTICE file(s) distributed with this work for additional
* information regarding copyright ownership.
*
* This program and the accompanying materials are made available under the
* terms of the Apache License, Version 2.0 which is available at
* https://www.apache.org/licenses/LICENSE-2.0.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*
* SPDX-License-Identifier: Apache-2.0
********************************************************************************/
package org.eclipse.tractusx.valueaddedservice.repository;

import org.eclipse.tractusx.valueaddedservice.domain.DataSource;
import org.eclipse.tractusx.valueaddedservice.domain.DataSourceValue;
import org.eclipse.tractusx.valueaddedservice.dto.DataDTO;
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

    @Query("select new org.eclipse.tractusx.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.iso2 in ?2 " +
            "and ds.dataSourceName in ?3 "+
            "and ds.yearPublished = ?4 "+
            "and dsv.score > ?1 ")
    List<DataDTO> findByRatingAndCountryAndScoreGreaterThanAndYear(Float score, List<String> country, List<String> dataSources, Integer year);

    @Query("select new org.eclipse.tractusx.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and ds.dataSourceName in ?3 "+
            "and dsv.score > ?1 ")
    List<DataDTO> findByRatingAndCountryAndScoreGreaterThan(Float score, List<String> country, List<String> dataSources);


    @Query("select new org.eclipse.tractusx.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and ds.dataSourceName in ?1 ")
    List<DataDTO> findByRating(List<String> dataSources);


    List<DataSourceValue> findByDataSource(DataSource dataSource);

    @Query("select new org.eclipse.tractusx.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and ds.dataSourceName in ?1 " +
            "and ds.yearPublished = ?2 "+
            "and dsv.score > ?3 ")
    List<DataDTO> findByRatingAndScoreGreaterThanAndYear(List<String> dataSources, Integer year, Float score);

    @Query("select new org.eclipse.tractusx.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and ds.dataSourceName in ?1 " +
            "and dsv.score > ?2")
    List<DataDTO> findByRatingAndScoreGreaterThan(List<String> dataSources, Float score);
    @Query("select new org.eclipse.tractusx.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and ds.yearPublished = ?3 "+
            "and dsv.score > ?1 ")
    List<DataDTO> findByCountryScoreGreaterThanAndYear(Float score, List<String> country, Integer year);

    @Query("select new org.eclipse.tractusx.valueaddedservice.dto.DataDTO(dsv.country,dsv.score,ds.dataSourceName,dsv.iso3,dsv.iso2,dsv.continent) " +
            "from DataSource ds INNER JOIN DataSourceValue dsv " +
            "ON dsv.dataSource.id = ds.id " +
            "and dsv.country in ?2 " +
            "and dsv.score > ?1 ")
    List<DataDTO> findByCountryScoreGreaterThan(Float score, List<String> country);

}
