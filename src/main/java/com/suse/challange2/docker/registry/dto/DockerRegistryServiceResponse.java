package com.suse.challange2.docker.registry.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DockerRegistryServiceResponse {

    private List<String> repositories;

    public DockerRegistryServiceResponse(List<String> repositories) {
        this.repositories = new ArrayList<>(repositories);
    }

    public List<String> getRepositories() {
        return Collections.unmodifiableList(repositories);
    }
}
