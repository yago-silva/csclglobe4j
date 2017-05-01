package com.suse.challange2.docker.registry.client.feign;

import com.suse.challange2.docker.registry.client.dto.ListRepositoriesResponse;
import com.suse.challange2.docker.registry.client.dto.ListRepositoryImagesResponse;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/v2")
public interface FeignDockerRegistryStub {

    @GET
    @Path("/_catalog")
    public ListRepositoriesResponse listRepositories();


    @GET
    @Path("/{repositoryName}/tags/list")
    public ListRepositoryImagesResponse listImagesByRepository(
            final @PathParam("repositoryName") String repositoryName);

}
