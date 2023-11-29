#********************************************************************************
# Copyright (c) 2022,2023 BMW Group AG
# Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Apache License, Version 2.0 which is available at
# https://www.apache.org/licenses/LICENSE-2.0.
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations
# under the License.
#
# SPDX-License-Identifier: Apache-2.0
#*******************************************************************************/

FROM maven:3.8-openjdk-18 as maven

COPY ./pom.xml /pom.xml
COPY ./src ./src
COPY LICENSE NOTICE.md DEPENDENCIES SECURITY.md /app/

RUN mvn clean package -DskipTests


#CMD exec /bin/bash -c "trap : TERM INT; sleep infinity & wait"
# Copy the jar and build image
FROM eclipse-temurin:21-jre-alpine AS value-added-service

ARG UID=1000
ARG GID=1000

EXPOSE 8080
EXPOSE 433
EXPOSE 80

WORKDIR /app

COPY --chown=${UID}:${GID} --from=maven target/value-added-service-*.jar app.jar

RUN apk update && apk upgrade --no-cache libssl3 libcrypto3

USER ${UID}:${GID}

# Health check instruction
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# set the startup command to run your binary
CMD ["java", "-jar", "./app.jar"]




