package com.suse.challange2.docker.registry;

import com.suse.challange2.docker.registry.service.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.service.dto.ListRepositoryImagesResponse;
import com.suse.challange2.docker.registry.exceptions.HttpOperationFailedException;
import com.suse.challange2.docker.registry.service.DockerRegistryService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DockerRegistryClient {

    private DockerRegistryService dockerRegistryService;

    public DockerRegistryClient(DockerRegistryService dockerRegistryService) {
        this.dockerRegistryService = dockerRegistryService;
    }

    public List<String> listRepositories() {
        try {
            ListRepositoriesResponse responseBody = dockerRegistryService.listRepositories();
            return Collections.unmodifiableList(responseBody.getRepositories());
        } catch (RuntimeException | IOException e) {
            throw new HttpOperationFailedException(e.getMessage(), e);
        }
    }

    public List<String> listImagesByRepository(String repositoryName) {
        try {
            ListRepositoryImagesResponse responseBody = dockerRegistryService.listImagesByRepository(repositoryName);
            return Collections.unmodifiableList(responseBody.getTags());
        } catch (RuntimeException | IOException e) {
            throw new HttpOperationFailedException(e.getMessage(), e);
        }
    }
}