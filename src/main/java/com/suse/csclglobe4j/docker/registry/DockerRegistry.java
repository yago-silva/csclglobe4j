package com.suse.csclglobe4j.docker.registry;

import com.suse.csclglobe4j.docker.registry.exceptions.NetworkException;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoryImagesResponse;
import com.suse.csclglobe4j.docker.registry.client.DockerRegistryClient;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DockerRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerRegistry.class);

    private DockerRegistryClient dockerRegistryService;

    public DockerRegistry(DockerRegistryClient dockerRegistryService) {
        this.dockerRegistryService = dockerRegistryService;
    }

    public List<String> listRepositories() {
        try {
            LOGGER.info("Listing all repositories of docker registry");

            ListRepositoriesResponse responseBody = dockerRegistryService.listRepositories();
            List<String> availableRepositories = responseBody.getRepositories();

            LOGGER.info("%s repositories found", availableRepositories.size());

            return Collections.unmodifiableList(availableRepositories);
        } catch (IOException e) {
            throw new NetworkException("Error when try to list repositories", e);
        }
    }

    public List<DockerImage> listImagesByRepository(String repositoryName) {
        try {

            LOGGER.info("Listing all images of repository  '%s'", repositoryName);

            ListRepositoryImagesResponse responseBody = dockerRegistryService.listImagesByRepository(repositoryName);
            List<DockerImage> dockerImages = responseBody.getTags().stream()
                    .map(tag -> new DockerImage(repositoryName, tag)).collect(Collectors.toList());

            LOGGER.info("%s images found for repository '%s'", dockerImages.size(), repositoryName);

            return Collections.unmodifiableList(dockerImages);

        } catch (IOException e) {
            throw new NetworkException("Error when try to list images in repository: " + repositoryName, e);
        }
    }

    public List<DockerImage> listAllAvailableImages() {

        LOGGER.info("Listing all available images of docker registry");

        List<DockerImage> foundImages = listRepositories().stream()
                .map(repository -> listImagesByRepository(repository))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        LOGGER.info("%s images found in entire docker registry", foundImages.size());

        return foundImages;
    }
}