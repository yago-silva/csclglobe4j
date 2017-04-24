package com.suse.challange2.docker.registry.service;

import com.suse.challange2.docker.registry.dto.DockerRegistryServiceResponse;
import retrofit.http.GET;

import java.io.IOException;

public interface DockerRegistryService {

    @GET("/v2/_catalog")
    public DockerRegistryServiceResponse listRepositories() throws IOException;
}
