package com.suse.csclglobe4j.docker.registry.client.retrofit;

import com.suse.csclglobe4j.docker.registry.client.DockerRegistryClient;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoryImagesResponse;
import com.suse.csclglobe4j.docker.registry.exceptions.HttpOperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.IOException;

public class RetrofitDockerRegistryClient implements DockerRegistryClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrofitDockerRegistryClient.class);

    private RetrofitDockerRegistryStub retrofitDockerRegistryStub;

    public RetrofitDockerRegistryClient(RetrofitDockerRegistryStub retrofitDockerRegistryStub) {
        this.retrofitDockerRegistryStub = retrofitDockerRegistryStub;
    }

    @Override
    public ListRepositoriesResponse listRepositories() throws IOException, HttpOperationFailedException {
        try {
            LOGGER.debug("Trying listing all repositories using RetrofitDockerRegistryClient");
            return retrofitDockerRegistryStub.listRepositories();
        }catch (RetrofitError error){
            switch (error.getKind()){
                case HTTP:
                    Response response = error.getResponse();
                    throw new HttpOperationFailedException(response.getStatus(), response.getReason(), error);

                case NETWORK:
                    throw new IOException(error.getMessage(), error);

                default:
                    throw new RuntimeException(error.getMessage(), error);
            }
        }
    }

    @Override
    public ListRepositoryImagesResponse listImagesByRepository(String repositoryName)
            throws IOException, HttpOperationFailedException {
        try {
            LOGGER.debug("Trying listing images of repository '%s' using RetrofitDockerRegistryClient", repositoryName);
            return retrofitDockerRegistryStub.listImagesByRepository(repositoryName);
        }catch (RetrofitError error){
            switch (error.getKind()){
                case HTTP:
                    Response response = error.getResponse();
                    throw new HttpOperationFailedException(response.getStatus(), response.getReason(), error);

                case NETWORK:
                    throw new IOException(error.getMessage(), error);

                default:
                    throw new RuntimeException(error.getMessage(), error);
            }
        }
    }
}
