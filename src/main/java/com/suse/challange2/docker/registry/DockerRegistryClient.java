package com.suse.challange2.docker.registry;

import com.suse.challange2.docker.registry.service.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.service.dto.ListRepositoryImagesResponse;
import com.suse.challange2.docker.registry.exceptions.HttpOperationFailedException;
import com.suse.challange2.docker.registry.service.DockerRegistryService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<DockerImage> listImagesByRepository(String repositoryName) {
        try {
            ListRepositoryImagesResponse responseBody = dockerRegistryService.listImagesByRepository(repositoryName);
            List<DockerImage> dockerImages =
                    responseBody.getTags().stream().map(tag -> new DockerImage(repositoryName, tag)).collect(Collectors.toList());
            return Collections.unmodifiableList(dockerImages);
        } catch (RuntimeException | IOException e) {
            throw new HttpOperationFailedException(e.getMessage(), e);
        }
    }

    public List<DockerImage> listAllAvailableImages(){
        return listRepositories().stream()
                            .map(repository -> listImagesByRepository(repository))
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
    }
}