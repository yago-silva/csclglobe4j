package com.suse.challange2.docker.registry.service;

import com.suse.challange2.docker.registry.service.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.service.dto.ListRepositoryImagesResponse;
import retrofit.http.GET;
import retrofit.http.Path;

import java.io.IOException;
import com.suse.challange2.docker.registry.DockerRegistryClientBuilder;

/**
 * This interface is implemented by classes that knows how to perform request to docker registry public REST api.
 * In this project we use retrofit to create dynamic implementations to this interface
 * (see {@link DockerRegistryClientBuilder}).
 * */
public interface DockerRegistryService {

    /**
     * This method perform an http request to docker registry public REST api to get a list of available repositories.
     * Implentations should throw {@link IOException} for newtwork problems and #{@link RuntimeException} for error http
     * status code (out of range 200 - 299)
     * */
    @GET("/v2/_catalog")
    public ListRepositoriesResponse listRepositories() throws IOException, RuntimeException;


    /**
     * This method perform an http request to docker registry public REST api to get a list of available images on a
     * specific repository.
     * Implentations should throw {@link IOException} for newtwork problems and #{@link RuntimeException} for error http
     * status code (out of range 200 - 299)
     * */
    @GET("/v2/{repositoryName}/tags/list")
    public ListRepositoryImagesResponse listImagesByRepository(
            @Path("repositoryName") String repositoryName) throws IOException, RuntimeException;
}
