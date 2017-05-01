package com.suse.csclglobe4j.docker.registry.client.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListRepositoriesResponse {

    private List<String> repositories;

    ListRepositoriesResponse(){}

    public ListRepositoriesResponse(List<String> repositories) {
        this.repositories = new ArrayList<>(repositories);
    }

    public List<String> getRepositories() {
        return Collections.unmodifiableList(repositories);
    }
}
