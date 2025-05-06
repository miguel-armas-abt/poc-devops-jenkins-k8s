package com.demo.poc.commons.custom.config;

import org.mockserver.integration.ClientAndServer;

import java.io.IOException;

public interface MockService {

    void loadMocks(ClientAndServer mockServer) throws IOException;

}
