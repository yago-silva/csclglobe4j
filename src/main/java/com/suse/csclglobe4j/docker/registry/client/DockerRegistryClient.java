package com.suse.csclglobe4j.docker.registry.client;

import com.suse.csclglobe4j.docker.registry.exceptions.HttpOperationFailedException;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoryImagesResponse;

import java.io.IOException;

/**
 * This interface is an abstraction for docker registry clients (implementations that knows how to perform http requests
 * to Docker Registry Service).
 * The idea of this interface is to allow users to create custom implementations and extend this library.
 * Users can provide you own implementation using prefered http client libraries but csclglobe4j core we have two
 * implementations of this interface:
 * {@link com.suse.csclglobe4j.docker.registry.client.retrofit.RetrofitDockerRegistryClient} and
 * {@link com.suse.csclglobe4j.docker.registry.client.feign.FeignDockerRegistryClient}
 */

public interface DockerRegistryClient {

    /**
     * This method list all available repositories of DockerRegistry.
     * Implementations should throws an {@link IOException} for any network problem (like time out or service not
     * available).
     * Implementations should throws an {@link HttpOperationFailedException} for response with http status different than
     * 200 (success).
     * */
    public ListRepositoriesResponse listRepositories()
            throws IOException, HttpOperationFailedException;


    /**
     * This method list all available images in a specific reposity.
     * Implementations should throws an {@link IOException} for any network problem (like time out or service not
     * available).
     * Implementations should throws an {@link HttpOperationFailedException} for response with http status different than
     * 200 (success).
     * */
    public ListRepositoryImagesResponse listImagesByRepository(String repositoryName)
            throws IOException, HttpOperationFailedException;
}
