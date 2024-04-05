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
package org.eclipse.tractusx.valueaddedservice.service.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Service
@Slf4j
public class InvokeService {


    private WebClient poolWebClient;
    private WebClient gateWebClient;

    @Autowired
    public void ExternalService(@Qualifier("poolWebClient") WebClient poolWebClient,
                                @Qualifier("gateWebClient") WebClient gateWebClient) {
        this.poolWebClient = poolWebClient;
        this.gateWebClient = gateWebClient;
    }

    public <T> Mono<List<T>> executeRequest(String clientType,String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> responseType, Function<String, List<T>> mappingFunction) {
        WebClient webClient = getWebClient(clientType);
        return webClient.method(httpMethod)
                .uri(url)
                .headers(headers -> headers.addAll(httpEntity.getHeaders()))
                .body(BodyInserters.fromValue(httpEntity.getBody()))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(subscription -> {
                    // Logging request details
                    log.debug("Sending request to URL: {}", url);
                    log.debug("Request Headers: {}", httpEntity.getHeaders());
                    log.debug("Request Body: {}", httpEntity.getBody());
                })
                .map(mappingFunction)
                .onErrorResume(e -> {
                    log.error("error url {} message {}", url, e.getMessage());
                    throw new RuntimeException(e.getMessage());
                });
    }

    public <T> Mono<T> executeRequest(String clientType,String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, Function<String, T> mappingFunction) {
        WebClient webClient = getWebClient(clientType);
        return webClient.method(httpMethod)
                .uri(url)
                .headers(headers -> headers.addAll(httpEntity.getHeaders()))
                .body(BodyInserters.fromValue(httpEntity.getBody()))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(subscription -> {
                    // Logging request details
                    log.debug("Sending request to URL: {}", url);
                    log.debug("Request Headers: {}", httpEntity.getHeaders());
                    log.debug("Request Body: {}", httpEntity.getBody());
                })
                .map(mappingFunction)
                .onErrorResume(e -> {
                    log.error("Error url {} message {}", url, e.getMessage());
                    throw new RuntimeException(e.getMessage());
                });

    }

    public Mono<Boolean> executeRequest(String clientType,String url, HttpMethod httpMethod, HttpEntity<?> httpEntity) {
        WebClient webClient = getWebClient(clientType);
        return webClient.method(httpMethod)
                .uri(url)
                .headers(headers -> headers.addAll(httpEntity.getHeaders()))
                .body(BodyInserters.fromValue(httpEntity.getBody()))
                .retrieve()
                .toBodilessEntity()  // Ignores the body and only retrieves the status code
                .map(responseEntity -> responseEntity.getStatusCode().is2xxSuccessful())  // Maps to true if status code is 2xx
                .onErrorResume(e -> {
                    log.error("Error during request to {}: {}", url, e.getMessage());
                    throw new RuntimeException(e.getMessage());
                });
    }
    public <T> Mono<List<T>> executeRequest(String clientType,String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, TypeReference<List<T>> responseTypeRef, Function<String, List<T>> mappingFunction) {
        WebClient webClient = getWebClient(clientType);
        return webClient.method(httpMethod)
                .uri(url)
                .headers(headers -> headers.addAll(httpEntity.getHeaders()))
                .body(BodyInserters.fromValue(httpEntity.getBody()))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(subscription -> {
                    // Logging request details
                    log.debug("Sending request to URL: {}", url);
                    log.debug("Request Headers: {}", httpEntity.getHeaders());
                    log.debug("Request Body: {}", httpEntity.getBody());
                })
                .map(mappingFunction)
                .onErrorResume(e -> {
                    log.error("error url {} message {}", url, e.getMessage());
                    throw new RuntimeException(e.getMessage());
                });
    }


    private WebClient getWebClient(String clientType) {
        if(clientType.equals("pool")){
            return poolWebClient;
        }else if (clientType.equals("gate")){
            return gateWebClient;
        }else{
            return WebClient.builder().build();
        }
    }

    public <T> Mono<List<T>> executeRequest(String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> responseType, Function<String, List<T>> mappingFunction) {
        WebClient webClient = getWebClient("none");
        return webClient.method(httpMethod)
                .uri(url)
                .headers(headers -> headers.addAll(httpEntity.getHeaders()))
                .body(BodyInserters.fromValue(httpEntity.getBody()))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(subscription -> {
                    // Logging request details
                    log.debug("Sending request to URL: {}", url);
                    log.debug("Request Headers: {}", httpEntity.getHeaders());
                    log.debug("Request Body: {}", httpEntity.getBody());
                })
                .map(mappingFunction)
                .onErrorResume(e -> {
                    log.error("error url {} message {}", url, e.getMessage());
                    throw new RuntimeException(e.getMessage());
                });
    }
}


