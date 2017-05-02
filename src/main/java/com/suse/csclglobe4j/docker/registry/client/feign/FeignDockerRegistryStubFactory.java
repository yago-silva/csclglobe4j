package com.suse.csclglobe4j.docker.registry.client.feign;

import feign.Feign;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeignDockerRegistryStubFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignDockerRegistryStubFactory.class);

    public static FeignDockerRegistryStub get(String baseUrl, int timeout){

        FeignDockerRegistryStub stub = Feign.builder().contract(new JAXRSContract())
                .encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
                .options(new Request.Options(timeout, timeout))
                .target(FeignDockerRegistryStub.class, baseUrl);

        LOGGER.debug("A new FeignDockerRegistryStub has been created! Timeout was set to %s milliseconds " +
                "and base url was set to %s", timeout, baseUrl);

        return stub;
    }
}