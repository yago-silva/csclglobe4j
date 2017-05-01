package com.suse.csclglobe4j.docker.registry.client.retrofit;

import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.csclglobe4j.docker.registry.client.dto.ListRepositoryImagesResponse;
import retrofit.http.GET;
import retrofit.http.Path;

public interface RetrofitDockerRegistryStub {

    @GET("/v2/_catalog")
    public ListRepositoriesResponse listRepositories();

    @GET("/v2/{repositoryName}/tags/list")
    public ListRepositoryImagesResponse listImagesByRepository(
            @Path("repositoryName") String repositoryName);

}
