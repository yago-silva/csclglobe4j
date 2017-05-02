package com.suse.csclglobe4j.docker.registry.client.feign;

import com.suse.csclglobe4j.docker.registry.client.DockerRegistryClient;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoryImagesResponse;
import com.suse.csclglobe4j.docker.registry.exceptions.HttpOperationFailedException;
import feign.FeignException;
import feign.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FeignDockerRegistryClient implements DockerRegistryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignDockerRegistryClient.class);

    private FeignDockerRegistryStub feignDockerRegistryStub;

    public FeignDockerRegistryClient(FeignDockerRegistryStub feignDockerRegistryStub) {
        this.feignDockerRegistryStub = feignDockerRegistryStub;
    }

    @Override
    public ListRepositoriesResponse listRepositories() throws IOException, HttpOperationFailedException {
        try{
            LOGGER.debug("Trying listing all repositories using FeignDockerRegistryClient");
            return feignDockerRegistryStub.listRepositories();
        } catch (RetryableException ex){
            throw new IOException(ex.getMessage(), ex);
        } catch (FeignException ex){
            throw new HttpOperationFailedException(ex.status(), ex.getMessage(), ex);
        }
    }

    @Override
    public ListRepositoryImagesResponse listImagesByRepository(String repositoryName) throws IOException, HttpOperationFailedException {
        try {
            LOGGER.debug("Trying listing images of repository '%s' using FeignDockerRegistryClient", repositoryName);
            return feignDockerRegistryStub.listImagesByRepository(repositoryName);
        } catch (RetryableException ex){
            throw new IOException(ex.getMessage(), ex);
        } catch (FeignException ex){
            throw new HttpOperationFailedException(ex.status(), ex.getMessage(), ex);
        }
    }
}
