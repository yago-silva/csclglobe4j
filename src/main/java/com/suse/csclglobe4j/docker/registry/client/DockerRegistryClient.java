package com.suse.csclglobe4j.docker.registry.client;

import com.suse.csclglobe4j.docker.registry.exceptions.HttpOperationFailedException;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoryImagesResponse;

import java.io.IOException;

public interface DockerRegistryClient {

    public ListRepositoriesResponse listRepositories()
            throws IOException, HttpOperationFailedException;

    public ListRepositoryImagesResponse listImagesByRepository(String repositoryName)
            throws IOException, HttpOperationFailedException;
}
