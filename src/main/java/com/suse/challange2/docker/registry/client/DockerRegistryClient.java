package com.suse.challange2.docker.registry.client;

import com.suse.challange2.docker.registry.exceptions.HttpOperationFailedException;
import com.suse.challange2.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.client.dto.ListRepositoryImagesResponse;

import java.io.IOException;

public interface DockerRegistryClient {

    public ListRepositoriesResponse listRepositories()
            throws IOException, HttpOperationFailedException;

    public ListRepositoryImagesResponse listImagesByRepository(String repositoryName)
            throws IOException, HttpOperationFailedException;
}
