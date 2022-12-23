package com.catenax.valueaddedservice.service.mocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ConfigServerMock {

    public static WireMockServer wireMockServer = new WireMockServer(8585);
    public static boolean flag = true;
    private static final Object lock = new Object();

    public static WireMockServer openPort(WireMockServer server) throws InterruptedException {

        synchronized (lock) {
            if (!wireMockServer.isRunning())
                wireMockServer.start();
            server = wireMockServer;
        }
        //Opening Port one time only, the first thread opens the port
        return server;
    }

    public static WireMockServer closePort(WireMockServer server) {
        wireMockServer = server;
        wireMockServer.stop();
        wireMockServer.shutdown();
        flag = false;
        return wireMockServer;
    }

    public static void mocKConnectionToExternalBpnApi(WireMockServer mockServer, List list) throws JsonProcessingException {

        mockServer.stubFor(get("/api/v1/users")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().writeValueAsString(list))));
    }

    public static void mocKConnectionToExternalBpnApiError(WireMockServer mockServer) {

        mockServer.stubFor(get("/api/v1/usersError")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)));
    }


}
