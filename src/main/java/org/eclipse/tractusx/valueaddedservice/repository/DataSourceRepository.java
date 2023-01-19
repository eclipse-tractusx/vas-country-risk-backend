/********************************************************************************
* Copyright (c) 2022,2023 BMW Group AG 
* Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
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
import org.eclipse.tractusx.valueaddedservice.domain.enumeration.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the DataSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {

    List<DataSource> findByYearPublishedAndType(Integer year, Type type);

    List<DataSource> findByYearPublishedAndCompanyUserNameAndCompanyUserEmailAndCompanyUserCompanyNameAndType(Integer year, String name, String email, String company, Type type);

    List<DataSource> findByCompanyUserNameAndCompanyUserEmailAndCompanyUserCompanyNameOrTypeOrTypeAndCompanyUserCompanyName(String name, String email, String company, Type type, Type typeCompany , String companyName );

    List<DataSource> findByYearPublishedAndCompanyUserCompanyNameAndType(Integer year, String company, Type type);

}
