package com.suse.challange2.docker.registry.client.retrofit;

import com.suse.challange2.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.client.dto.ListRepositoryImagesResponse;
import retrofit.http.GET;
import retrofit.http.Path;

public interface RetrofitDockerRegistryStub {

    @GET("/v2/_catalog")
    public ListRepositoriesResponse listRepositories();

    @GET("/v2/{repositoryName}/tags/list")
    public ListRepositoryImagesResponse listImagesByRepository(
            @Path("repositoryName") String repositoryName);

}
