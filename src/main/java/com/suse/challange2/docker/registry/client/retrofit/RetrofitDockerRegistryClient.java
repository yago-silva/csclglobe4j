package com.suse.challange2.docker.registry.client.retrofit;

import com.suse.challange2.docker.registry.client.DockerRegistryClient;
import com.suse.challange2.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.client.dto.ListRepositoryImagesResponse;
import com.suse.challange2.docker.registry.exceptions.HttpOperationFailedException;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.IOException;

public class RetrofitDockerRegistryClient implements DockerRegistryClient {

    private RetrofitDockerRegistryStub retrofitDockerRegistryStub;

    public RetrofitDockerRegistryClient(RetrofitDockerRegistryStub retrofitDockerRegistryStub) {
        this.retrofitDockerRegistryStub = retrofitDockerRegistryStub;
    }

    @Override
    public ListRepositoriesResponse listRepositories() throws IOException, HttpOperationFailedException {
        try {
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
