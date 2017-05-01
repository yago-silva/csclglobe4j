package com.suse.challange2.docker.registry;

import com.suse.challange2.docker.registry.exceptions.NetworkException;
import com.suse.challange2.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.client.dto.ListRepositoryImagesResponse;
import com.suse.challange2.docker.registry.client.DockerRegistryClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DockerRegistry {

    private DockerRegistryClient dockerRegistryService;

    public DockerRegistry(DockerRegistryClient dockerRegistryService) {
        this.dockerRegistryService = dockerRegistryService;
    }

    public List<String> listRepositories() {
        try {
            ListRepositoriesResponse responseBody = dockerRegistryService.listRepositories();
            return Collections.unmodifiableList(responseBody.getRepositories());
        } catch (IOException e) {
            throw new NetworkException("Error when try to list repositories", e);
        }
    }

    public List<DockerImage> listImagesByRepository(String repositoryName) {
        try {

            ListRepositoryImagesResponse responseBody = dockerRegistryService.listImagesByRepository(repositoryName);
            List<DockerImage> dockerImages = responseBody.getTags().stream()
                    .map(tag -> new DockerImage(repositoryName, tag)).collect(Collectors.toList());
            return Collections.unmodifiableList(dockerImages);

        } catch (IOException e) {
            throw new NetworkException("Error when try to list images in repository: " + repositoryName, e);
        }
    }

    public List<DockerImage> listAllAvailableImages() {
        return listRepositories().stream()
                            .map(repository -> listImagesByRepository(repository))
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
    }
}