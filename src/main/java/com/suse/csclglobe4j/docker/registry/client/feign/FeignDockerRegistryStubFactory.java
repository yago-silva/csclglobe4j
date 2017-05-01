package com.suse.csclglobe4j.docker.registry.client.feign;


import feign.Feign;
import feign.Request;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;


public class FeignDockerRegistryStubFactory {

    public static FeignDockerRegistryStub get(String baseUrl, int timeout){

        return Feign.builder().contract(new JAXRSContract())
                .encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
                .options(new Request.Options(timeout, timeout))
                .target(FeignDockerRegistryStub.class, baseUrl);
    }
}
